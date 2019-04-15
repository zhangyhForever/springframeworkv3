package springframework.test.controller;

import com.forever.springframework.annotation.GPAutowired;
import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPRequestMapping;
import com.forever.springframework.annotation.GPRequestPram;
import com.forever.springframework.test.service.IActionService;

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
    public String test(@GPRequestPram("name") String name){

        return "";
    }
}
