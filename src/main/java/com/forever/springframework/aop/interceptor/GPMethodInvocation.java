package com.forever.springframework.aop.interceptor;

import com.forever.springframework.aop.aspest.GPJointPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/26
 */
public class GPMethodInvocation implements GPJointPoint {

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] arguments;
    private Class<?> targetClass;
    private List<Object> interceptionsAndDynamicMethodMatchers;
    private Map<String, Object> userAttributes;

    private Integer currentInterceptorIndex = -1;

    public GPMethodInvocation(Object proxy, Object target, Method method,
                              Object[] arguments, Class<?> targetClass,
                              List<Object> interceptionsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptionsAndDynamicMethodMatchers = interceptionsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable{
        if(this.currentInterceptorIndex == this.interceptionsAndDynamicMethodMatchers.size() - 1){
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptionAdvice = this.interceptionsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if(interceptorOrInterceptionAdvice instanceof GPMethodInterceptor){
            GPMethodInterceptor m1 = (GPMethodInterceptor) interceptorOrInterceptionAdvice;
            return m1.invoke(this);
        }else{
            return proceed();
        }
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
            if(this.userAttributes == null){
                userAttributes = new HashMap<String, Object>();
            }
            this.userAttributes.put(key, value);
        }else{
            if(this.userAttributes != null){
                this.userAttributes.remove(key);
            }
        }
    }

    public Object getUserAttribute(String key) {
        return userAttributes.get(key) != null ? userAttributes.get(key): null;
    }
}
