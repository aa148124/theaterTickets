package com.dkh.controller;

import com.dkh.dto.Result;
import com.dkh.pojo.Order;
import com.dkh.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @GetMapping("/getOrderList/{current}/{size}")
    public Result getOrderList(@PathVariable Integer current, @PathVariable Integer size) {
        return orderService.getOrderListByPage(current, size);
    }
    @GetMapping("/getOrderById/{id}")
    public Result getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    /**
     * 根据用户id查询订单
     *
     * @param userId  用户id
     * @param current 页
     * @param size    大小
     * @return
     */
    @GetMapping("/getOrdersByUserId/{userId}/{current}/{size}")
    public Result getOrdersByUserId(@PathVariable Long userId,
                                    @PathVariable Integer current,
                                    @PathVariable Integer size) {
        return orderService.getOrdersByUserId(userId, current, size);
    }
    @GetMapping("/getOrderTotalByUserId/{userId}")
    public Result getOrderTotalByUserId(@PathVariable Long userId) {
        return orderService.getOrderTotalByUserId(userId);
    }
    @GetMapping("/getOrderTotal")
    public Result getOrderTotal() {
        return orderService.getTotal();
    }

    @PostMapping("/searchOrder/{current}/{size}")
    public Result searchOrder(Long id, Long userId, Long arrangeId,
                              @PathVariable Integer current, @PathVariable Integer size) {
        //判断是否全为空
        if (id == null && userId == null && arrangeId == null) {
            return orderService.getOrderListByPage(current, current);
        }
        int currentPage = (current - 1) * size;
        //查询数据库
        List<Order> userList = orderService.searchOrderByCondition(id, userId, arrangeId, currentPage, size);
        return new Result(200, userList);
    }

    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody Order order, HttpServletRequest request) throws JsonProcessingException {
        System.out.println("order = " + order);
        return orderService.addOrder(order, request);
    }

    @PutMapping("/pay")
    public Result pay(Long id){
        return orderService.pay(id);
    }
}
