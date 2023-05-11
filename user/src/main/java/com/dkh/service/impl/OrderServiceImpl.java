package com.dkh.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.Result;
import com.dkh.mapper.OrderMapper;
import com.dkh.pojo.Arrange;
import com.dkh.pojo.Order;
import com.dkh.service.FilmService;
import com.dkh.service.OrderService;
import com.dkh.utils.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dkh.utils.RabbitMQConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private FilmService filmService;

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public Result getOrderListByPage(Integer current, Integer size) {
        IPage page = new Page(current, size).addOrder(OrderItem.desc("order_time"));
        page(page);
        List orders = page.getRecords();
        return new Result(200, orders);
    }

    @Override
    public Result getTotal() {
        int count = count();
        return new Result(200, count);
    }

    @Override
    public List<Order> searchOrderByCondition(Long id, Long userId, Long arrangeId, Integer current, Integer size) {
        return orderMapper.searchByCondition(id, userId, arrangeId, current, size);
    }

    @Override
    public Result getOrdersByUserId(Long userId, Integer current, Integer size) {
        //查询该用户的订单信息
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        Page<Order> page = new Page(current, size).addOrder(OrderItem.desc("order_time"));
        List<Order> orders = page(page, qw).getRecords();
        //获取排片信息
        for (Order order : orders) {
            //获取排片id
            Long arrangeId = order.getArrangeId();
            //远程调用查询排片信息
            Arrange arrange = getArrangeById(order.getArrangeId());
            order.setArrange(arrange);
        }
        return new Result(200, orders);
    }

    @Override
    public Result getOrderTotalByUserId(Long userId) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        int count = count(qw);
        return new Result(200, count);
    }

    /**
     * 下单
     * @param order
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addOrder(Order order, HttpServletRequest request) throws JsonProcessingException {
        //获取用户id
        String token = request.getHeader("token");
        //判断用户是否已经登录
        if (StringUtils.isBlank(token)){
            return new Result(400,null,"用户未登录");
        }
        Long userId = JWTUtils.getUserId(token);
        //获取排片信息
        Arrange arrange = getArrangeById(order.getArrangeId());
        //获取座位信息
        String seatStr = arrange.getSeat();
        //将座位转成int二维数组
        ObjectMapper objectMapper = new ObjectMapper();
        int[][] seat = objectMapper.readValue(seatStr, int[][].class);
        //获取座位坐标
        String[] str = order.getSeat().replace("座 ", ",").split(",");
        for (String s : str) {
            String[] s1 = s.split("排");
            for (int i = 0; i < s1.length; i += 2) {
                //s1[i]排s1[i+1]座
                int x = Integer.valueOf(s1[i]);//排
                int y = Integer.valueOf(s1[i + 1]);//座
                //判断座位是否已经被选
                if (seat[x - 1][y - 1] == 1) {
                    return new Result(400, null, "座位已经有人选了");
                }
                seat[x - 1][y - 1] = 1;
            }
        }
        //更新库存
        arrange.setSeat(JSONUtil.toJsonStr(seat));
        arrange.setStock(arrange.getStock() - str.length);
        Result result = filmService.updateArrange(arrange);
        if (result.getCode() != 200) {
            return new Result(400, null);
        }
        //封装order
        order.setUserId(userId);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(1);
        //保存到数据库
        boolean isSuccess = save(order);
        if (!isSuccess) {
            return new Result(400, null, "下单失败");
        }
        //返回订单id
        //发送消息到延迟队列,延迟时间为15分钟，超过15分钟订单取消
        sendToOrder(order.getId().toString(), 900000);
        return new Result(200, order);
    }

    public void sendToOrder(String message, Integer delayTime) {
        log.info("发送一条消息：" + message);
        // 三个参数
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME,
                DELAYED_ROUTING_KEY, message,
                message1 -> {
                    message1.getMessageProperties().setDelay(delayTime);
                    return message1;
                });
    }

    @Override
    public Result getOrderById(Long id) {
        Order order = query().eq("id", id).one();
        //获取下单时间
        LocalDateTime orderTime = order.getOrderTime();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //截至时间=下单时间+15分钟
        long end = orderTime.toEpochSecond(ZoneOffset.of("+8")) + 15 * 60;
        long time = end - now.toEpochSecond(ZoneOffset.of("+8"));
        Map map = new HashMap<>();
        map.put("order", order);
        map.put("time", time);
        return new Result(200, map);
    }

    /**
     * 支付功能
     * @param id 订单id
     * @return
     */
    @Override
    public Result pay(Long id) {
        //查询该订单是否存在
        Order order = query().eq("id", id).one();
        if (order == null) {
            return new Result(400, null, "订单不存在");
        }
        //判断订单是否已支付或者已经失效
        if (order.getStatus() == 2 || order.getStatus() == 0)  {
            return new Result(400, null, "订单已经支付过了");
        }
        //修改订单状态
        order.setStatus(2);
        //支付时间
        order.setPaymentTime(LocalDateTime.now());
        //更新数据库
        boolean isSuccess = update().eq("id", order.getId()).update(order);
        //发送订单id到消息队列，处理对票房的增加
        sendToBoxOffice(order.getId().toString(), 0);
        if (isSuccess) {
            return new Result(200, null, "支付成功");
        }
        return new Result(400, null, "支付失败");
    }

    /**
     * 发送消息到票房队列
     *
     * @param message
     * @param delayTime
     */
    public void sendToBoxOffice(String message, Integer delayTime) {
        log.info("发送一条消息：" + message);
        // 三个参数
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME,
                BOX_OFFICE_KEY, message,
                message1 -> {
                    message1.getMessageProperties().setDelay(delayTime);
                    return message1;
                });
    }

    public Arrange getArrangeById(Long arrangeId) {
        System.out.println("arrangeId = " + arrangeId);
        Result result = filmService.getArrangeById(arrangeId);
        String jsonStr = JSONUtil.toJsonStr(result.getData());
        Arrange arrange = JSONUtil.toBean(jsonStr, Arrange.class);
        return arrange;
    }
}
