package com.dkh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkh.dto.Result;
import com.dkh.pojo.Order;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
public interface OrderService extends IService<Order> {

    Result getOrderListByPage(Integer current, Integer size);

    Result getTotal();

    List<Order> searchOrderByCondition(Long id, Long userId, Long arrangeId,Integer current,Integer size);

    Result getOrdersByUserId(Long userId, Integer current, Integer size);

    Result getOrderTotalByUserId(Long userId);

    Result addOrder(Order order, HttpServletRequest request) throws JsonProcessingException;

    Result getOrderById(Long id);

    Result pay(Long id);
}
