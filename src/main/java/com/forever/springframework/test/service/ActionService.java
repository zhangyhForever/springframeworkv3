package com.forever.springframework.test.service;

import com.forever.springframework.annotation.GPService;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
@GPService("actionService")
public class ActionService implements IActionService {
    public String getName() {
        return "张永恒";
    }
}
