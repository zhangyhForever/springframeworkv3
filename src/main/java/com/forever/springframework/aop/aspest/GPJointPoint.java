package com.forever.springframework.aop.aspest;

import java.lang.reflect.Method;

/**
 * @Description: 连接点，具体的需要织入其他操作的方法
 * @Author: zhang
 * @Date: 2019/4/27
 */
public interface GPJointPoint {

    Object getThis();

    Method getMethod();

    Object[] getArguments();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
