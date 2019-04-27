package com.forever.springframework.aop.aspest;

import com.forever.springframework.aop.interceptor.GPMethodInterceptor;
import com.forever.springframework.aop.interceptor.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/27
 */
public class BPAfterThrowingAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

    private String throwingName;

    public BPAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public Object invoke(GPMethodInvocation mi) throws Throwable {
        try{
            return mi.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowingName(String throwingName){
        this.throwingName = throwingName;
    }
}
