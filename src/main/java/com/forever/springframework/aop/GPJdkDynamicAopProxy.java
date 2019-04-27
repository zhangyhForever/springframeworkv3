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

    private GPAdvisedSupport advised;

    public GPJdkDynamicAopProxy(GPAdvisedSupport config){
        this.advised = config;
    }

    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptiorsAndDynamicMethodMatchers = advised.getInterceptorsAndDynammicInterceptionAdvice(method, this.advised.getTargetClass());
        GPMethodInvocation invocation = new GPMethodInvocation(
                proxy, null, method, args,
                advised.getTargetClass(), interceptiorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
