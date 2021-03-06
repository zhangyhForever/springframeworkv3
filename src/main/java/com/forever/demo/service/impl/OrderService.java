package com.forever.demo.service.impl;

import com.forever.springframework.annotation.GPService;
import com.forever.demo.model.Order;
import com.forever.demo.service.IOrderService;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
@GPService(value="orderService")
public class OrderService implements IOrderService {

    public String insertOrder(Order order) {
        String msg = ("创建订单成功，订单号====》"+order.getOrderId());
        return msg;
    }
}
