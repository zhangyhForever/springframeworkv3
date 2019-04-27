package com.forever.springframework.demo.service.impl;

import com.forever.springframework.annotation.GPService;
import com.forever.springframework.demo.service.IActionService;

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
