package com.forever.demo.model;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public class Order {

    private String orderId;
    private String Username;
    private double price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
