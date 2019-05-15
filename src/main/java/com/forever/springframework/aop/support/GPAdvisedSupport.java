package com.forever.springframework.aop.support;

import com.forever.springframework.aop.aspest.BPAfterReturningAdviceInterceptor;
import com.forever.springframework.aop.aspest.BPAfterThrowingAdviceInterceptor;
import com.forever.springframework.aop.config.GPAopConfig;
import com.forever.springframework.aop.aspest.BPMethodBeforeAdviceInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: aop配置文件解析类
 * @Author: zhang
 * @Date: 2019/4/26
 */
@Slf4j
public class GPAdvisedSupport {

    private Class<?> targetClass;
    private Object target;
    private GPAopConfig config;
    private Pattern pointCutClassPattern;
    private Map<Method, List<Object>> methodCache;

    public GPAdvisedSupport(GPAopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass(){
        return this.targetClass;
    }

    public Object getTarget(){
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptionsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        if(null == cached){
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            methodCache.put(m, cached);
        }
        return cached;
    }

    public void setTargetClass(Class<?> targetClass) throws InstantiationException, IllegalAccessException {
        this.targetClass = targetClass;
        parse();
    }

    //解析targetClass(spring表达式)
    private void parse() throws IllegalAccessException, InstantiationException {
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //切入点表达式：pointCut=public .* com\.forever\.springframework\.demo\.service\..*Service\..*\(.*\)
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(")-4);

        //对切入点类进行正则编译
        pointCutClassPattern = Pattern.compile("class "+pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();

        //对切入点进行正则编译
        Pattern pattern = Pattern.compile(pointCut);

        Class<?> aspectClass = null;
        try {
            aspectClass = Class.forName(config.getAspectClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Method> aspectMethods = new HashMap<String, Method>();
        for (Method m : aspectClass.getMethods()) {
            aspectMethods.put(m.getName(), m);
        }

        for (Method m : this.targetClass.getMethods()) {
            String methodString = m.toString();
            if(methodString.contains("throws")){
                methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
            }
            Matcher methodMatcher = pattern.matcher(methodString);
            if(methodMatcher.matches()){
                List<Object> advices = new LinkedList<Object>();
                //把每一个方法包装成MethodInterceptor
                //before
                if(!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))){
                    advices.add(new BPMethodBeforeAdviceInterceptor(
                            aspectMethods.get(this.config.getAspectBefore()), aspectClass.newInstance()));
                }
                //after
                if(!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))){
                    advices.add(new BPAfterReturningAdviceInterceptor(
                            aspectMethods.get(this.config.getAspectAfter()), aspectClass.newInstance()
                    ));
                }
                //afterThrow
                if(!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow()))){
                    BPAfterThrowingAdviceInterceptor afterThrowingAdvice =
                            new BPAfterThrowingAdviceInterceptor(aspectMethods.get(this.config.getAspectAfterThrow())
                            , aspectClass.newInstance());
                    afterThrowingAdvice.setThrowingName(this.config.getAspectAfterThrowingName());
                    advices.add(afterThrowingAdvice);
                }
                methodCache.put(m, advices);
            }
        }
    }

    public boolean pointCutMatch() {
        return this.pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }
}
