package com.forever.springframework.beans.config;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/16
 */
public class GPBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object instance, String beanName){
        return instance;
    }

    public Object postProcessAfterInitialization(Object instance, String beanName){
        return instance;
    }
}
