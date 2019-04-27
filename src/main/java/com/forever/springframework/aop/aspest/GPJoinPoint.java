package com.forever.springframework.aop.aspest;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/27
 */
public interface GPJoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
