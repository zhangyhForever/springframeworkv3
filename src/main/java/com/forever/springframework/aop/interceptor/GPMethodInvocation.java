package com.forever.springframework.aop.interceptor;

import com.forever.springframework.aop.aspest.GPJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
public class GPMethodInvocation implements GPJoinPoint {

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] arguments;
    private Class<?> targetClass;
    private List<Object> interceptiorsAndDynamicMethodMatchers;
    private Map<String, Object> userAttrebutes;

    private Integer currentInterceptorIndex;

    public GPMethodInvocation(Object proxy, Object target, Method method,
                              Object[] arguments, Class<?> targetClass,
                              List<Object> interceptiorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptiorsAndDynamicMethodMatchers = interceptiorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable{
        if(this.currentInterceptorIndex == this.interceptiorsAndDynamicMethodMatchers.size() - 1){
            return invokeJoinpoint();
        }
        Object interceptorOrInterceptionAdvice = this.interceptiorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if(interceptorOrInterceptionAdvice instanceof GPMethodInterceptor){
            GPMethodInterceptor m1 = (GPMethodInterceptor) interceptorOrInterceptionAdvice;
            return m1.invoke(this);
        }else{
            return proceed();
        }
    }

    private Object invokeJoinpoint() {
        return null;
    }

    public Object getThis() {
        return this.target;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setUserAttribute(String key, Object value) {
        if(value != null){
            if(this.userAttrebutes == null){
                userAttrebutes = new HashMap<String, Object>();
            }
            this.userAttrebutes.put(key, value);
        }else{
            if(this.userAttrebutes != null){
                this.userAttrebutes.remove(key);
            }
        }
    }

    public Object getUserAttribute(String key) {
        return userAttrebutes.get(key) != null ? userAttrebutes.get(key): null;
    }
}
