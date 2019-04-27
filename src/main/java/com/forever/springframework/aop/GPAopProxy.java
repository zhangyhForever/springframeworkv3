package com.forever.springframework.aop;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
public interface GPAopProxy {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
