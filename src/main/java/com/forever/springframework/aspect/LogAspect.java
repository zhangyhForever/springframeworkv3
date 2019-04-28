package com.forever.springframework.aspect;

import com.forever.springframework.aop.aspest.GPJointPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
@Slf4j
public class LogAspect {

    public void before(GPJointPoint joinPoint){
        //记录方法开始执行时间
        log.info("Invoker Before Method :"+ joinPoint.getMethod() +
            "\nTargetObject:" + joinPoint.getThis() +
            "\nArgs:" + joinPoint.getArguments());
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
    }

    public void after(GPJointPoint joinPoint){
        //系统当前时间-方法开始执行时间=方法执行的时间
        log.info("Invoker After Method :"+ joinPoint.getMethod() +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + joinPoint.getArguments());
        Long startTime = (Long)joinPoint.getUserAttribute("startTime_"+ joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("user time:" + (endTime - startTime));
    }

    public void afterThrowing(GPJointPoint joinPoint, Throwable ex){
        //异常检测，可以拿到异常信息
        log.info("出现异常 :" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nException:" + ex.getCause());
    }
}
