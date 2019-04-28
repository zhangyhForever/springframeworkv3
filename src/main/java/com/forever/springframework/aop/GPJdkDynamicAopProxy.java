package com.forever.springframework.aop;

import com.forever.springframework.aop.interceptor.GPMethodInvocation;
import com.forever.springframework.aop.support.GPAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler {

    private GPAdvisedSupport config;

    public GPJdkDynamicAopProxy(GPAdvisedSupport config){
        this.config = config;
    }

    public Object getProxy() {
        ClassLoader classLoader = this.config.getTargetClass().getClassLoader();
        return getProxy(classLoader);
    }

    public Object getProxy(ClassLoader classLoader) {
        Class<?>[] interfaces = this.config.getTargetClass().getInterfaces();
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptionsAndDynamicMethodMatchers = config.getInterceptionsAndDynamicInterceptionAdvice(method, this.config.getTargetClass());
        GPMethodInvocation invocation = new GPMethodInvocation(
                proxy, config.getTarget(), method, args,
                config.getTargetClass(), interceptionsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
