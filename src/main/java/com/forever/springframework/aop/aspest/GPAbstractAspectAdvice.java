package com.forever.springframework.aop.aspest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/27
 */
public class GPAbstractAspectAdvice implements GPAdvice{

    private Method aspectMethod;

    private Object aspectTarget;

    public GPAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public  Object invokeAdviceMethod(GPJointPoint joinPoint, Object returnValue, Throwable tx) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if(null == parameterTypes || parameterTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            Object[] args = new Object[parameterTypes.length];
            for(int i=0; i<parameterTypes.length; i++){
                if(parameterTypes[i] == GPJointPoint.class){
                    args[i] = joinPoint;
                }else if(parameterTypes[i] == Throwable.class){
                    args[i] = tx;
                }else if(parameterTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(this.aspectTarget, args);
        }
    }
}
