package com.forever.springframework.aop;

import com.forever.springframework.aop.support.GPAdvisedSupport;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
public class GPCglibAopProxy implements GPAopProxy{

    private GPAdvisedSupport advised;

    public GPCglibAopProxy(GPAdvisedSupport advised) {
        this.advised = advised;
    }

    public Object getProxy() {
        return null;
    }

    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
