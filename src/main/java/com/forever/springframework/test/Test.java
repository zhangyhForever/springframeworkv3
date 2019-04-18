package com.forever.springframework.test;

import com.forever.springframework.test.service.ActionService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */

@Slf4j
public class Test {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, URISyntaxException {
        ActionService actionService = new ActionService();
        String resource = actionService.getClass().getResource("/").getPath();
        File file = new File(new File(resource).getParentFile().getPath()).getParentFile();
        System.out.println(file.getPath());
    }
}
