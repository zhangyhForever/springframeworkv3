package com.forever.springframework.test.controller;

import com.forever.springframework.annotation.GPAutowired;
import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPRequestMapping;
import com.forever.springframework.annotation.GPRequestPram;
import com.forever.springframework.test.service.IActionService;
import com.forever.springframework.webmvc.servlet.GPModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */

@GPController
@GPRequestMapping(value="/action")
public class ActionController {

    @GPAutowired
    private IActionService actionService;

    @GPRequestMapping(value="/test.do")
    public GPModelAndView test(@GPRequestPram("name") String name){
        Map<String, Object> model = new HashMap<String, Object>();
        String data = actionService.getName();
        model.put("data", data);
        model.put("teacher", name);
        return new GPModelAndView("first",model);
    }
}
