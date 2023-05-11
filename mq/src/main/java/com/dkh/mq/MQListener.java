package com.dkh.mq;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dkh.mapper.ArrangeMapper;
import com.dkh.mapper.FilmMapper;
import com.dkh.mapper.OrderMapper;
import com.dkh.pojo.Arrange;
import com.dkh.pojo.Film;
import com.dkh.pojo.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.dkh.utils.RabbitMQConstants.*;

@Component
public class MQListener {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private ArrangeMapper arrangeMapper;

    @RabbitListener(queues = DELAYED_QUEUE_ORDER)
    public void receive(Message message) throws JsonProcessingException {
        System.out.println("收到消息:" + new String(message.getBody()));
        //获取订单的Id
        String orderId = new String(message.getBody());
        //根据id查询订单是否已支付
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("id", orderId);
        Order order = orderMapper.selectOne(qw);
        //判断订单状态
        if (order != null && order.getStatus() == 1){
            //还处于未支付
            //修改排片的信息
            Arrange arrange = arrangeMapper.selectById(order.getArrangeId());
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
                    seat[x - 1][y - 1] = 0;
                }
            }
            //回滚库存
            arrange.setSeat(JSONUtil.toJsonStr(seat));
            arrange.setStock(arrange.getStock() + str.length);
            arrangeMapper.updateById(arrange);
            //设置订单状态为已取消
            order.setStatus(0);
            //修改数据库
            orderMapper.updateById(order);
        }
    }

    @RabbitListener(queues = DELAYED_QUEUE_BOX_OFFICE)
    public void addBoxOffice(Message message){
        System.out.println("收到消息:" + new String(message.getBody()));
        //获取订单的Id
        String orderId = new String(message.getBody());
        //查询订单信息
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("id", orderId);
        Order order = orderMapper.selectOne(qw);
        //获取排片id
        Long arrangeId = order.getArrangeId();
        //获取排片信息
        Arrange arrange = arrangeMapper.getArrangeById(arrangeId);
        //获取电影id
        Long filmId = arrange.getFilmId();
        //增加票房
        UpdateWrapper<Film> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", filmId).setSql("box_office = box_office + " + order.getPrice());
        filmMapper.update(null, updateWrapper);
    }

    /**
     * 修改电影上映状态
     * @param message
     */
    @RabbitListener(queues = DELAYED_QUEUE_FILM)
    public void filmRelease(Message message){
        //获取电影id
        String filmId = new String(message.getBody());
        //更新电影状态
        UpdateWrapper<Film> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", filmId).setSql("status=0");
        filmMapper.update(null, updateWrapper);
    }
}
