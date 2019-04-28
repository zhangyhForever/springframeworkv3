package com.forever.springframework.aop.aspest;

import com.forever.springframework.aop.interceptor.GPMethodInterceptor;
import com.forever.springframework.aop.interceptor.GPMethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/27
 */
public class BPMethodBeforeAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJointPoint joinPoint;

    public BPMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws InvocationTargetException, IllegalAccessException {
        super.invokeAdviceMethod(this.joinPoint, null, null);
    }

    public Object invoke(GPMethodInvocation mi) throws Throwable {
        //从被织入的代码中拿到，JoinPoint
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
