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
public class BPAfterReturningAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJoinPoint joinPoint;

    public BPAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public Object invoke(GPMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws InvocationTargetException, IllegalAccessException {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
