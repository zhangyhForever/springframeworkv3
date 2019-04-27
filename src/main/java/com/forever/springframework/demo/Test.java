package com.forever.springframework.demo;

import com.forever.springframework.demo.service.impl.ActionService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */

@Slf4j
public class Test {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, URISyntaxException {
        Class<ActionService> clazz = ActionService.class;
        for (Method m : clazz.getMethods()) {
            System.out.println(m);
        }
    }
}
