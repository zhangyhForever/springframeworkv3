package com.forever.demo.controller;

import com.forever.springframework.annotation.GPAutowired;
import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPRequestMapping;
import com.forever.springframework.annotation.GPRequestPram;
import com.forever.demo.model.Order;
import com.forever.demo.service.IOrderService;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */

@GPController()
@GPRequestMapping(value="/order")
public class OderController {

    @GPAutowired
    private IOrderService orderService;

    @GPRequestMapping("/create")
    public void create(@GPRequestPram("id") String orderId){
        Order order = new Order();
        order.setOrderId(orderId);
        String s = orderService.insertOrder(order);
        System.out.println(s);
    }

    @GPRequestMapping("/exception")
    public void exception(){
        throw new RuntimeException("自己故意抛的异常");
    }
}
