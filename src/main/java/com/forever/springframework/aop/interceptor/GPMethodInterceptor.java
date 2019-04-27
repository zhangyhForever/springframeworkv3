package com.forever.springframework.aop.interceptor;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/27
 */
public interface GPMethodInterceptor {

    Object invoke(GPMethodInvocation invocatoin) throws Throwable;
}
