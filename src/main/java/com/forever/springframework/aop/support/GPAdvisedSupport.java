package com.forever.springframework.aop.support;

import com.forever.springframework.aop.aspest.BPAfterReturningAdviceInterceptor;
import com.forever.springframework.aop.aspest.BPAfterThrowingAdviceInterceptor;
import com.forever.springframework.aop.config.GPAopConfig;
import com.forever.springframework.aop.aspest.BPMethodBeforeAdviceInterceptor;

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
public class GPAdvisedSupport {

    private Class<?> targetClass;
    private Object target;
    private GPAopConfig config;
    private Pattern pointCutClassPattern;
    private Map<Method, List<Object>> methodCache = new HashMap<Method, List<Object>>();

    public GPAdvisedSupport(GPAopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass(){
        return this.targetClass;
    }

    public void setTargetClass(Class<?> targetClass) throws InstantiationException, IllegalAccessException {
        this.targetClass = targetClass;
        parse();
    }

    //解析targetClass(spring表达式)
    private void parse() throws IllegalAccessException, InstantiationException {
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("(")-3);
        pointCutClassPattern = Pattern.compile("class "+pointCutForClassRegex.substring(
                pointCutForClassRegex.indexOf(" ") + 1));

        Pattern pattern = Pattern.compile(pointCut);

        Class<?> aspectClass = null;
        try {
            aspectClass = Class.forName(config.getAspectClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Method> aspectMetods = new HashMap<String, Method>();
        for (Method m : aspectClass.getMethods()) {
            aspectMetods.put(m.getName(), m);
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
                this.config.getAspectBefore();
                if(!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))){
                    advices.add(new BPMethodBeforeAdviceInterceptor(
                            aspectMetods.get(this.config.getAspectBefore()), aspectClass.newInstance()));
                }
                //after
                if(!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))){
                    advices.add(new BPAfterReturningAdviceInterceptor(
                            aspectMetods.get(this.config.getAspectAfter()), aspectClass.newInstance()
                    ));
                }
                //afterThrow
                if(!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow()))){
                    BPAfterThrowingAdviceInterceptor afterThrowingAdvice =
                            new BPAfterThrowingAdviceInterceptor(aspectMetods.get(this.config.getAspectAfterThrow())
                            , aspectClass.newInstance());
                    afterThrowingAdvice.setThrowingName(this.config.getAspectAfterThrowingName());
                    advices.add(afterThrowingAdvice);
                }
                methodCache.put(m, advices);
            }
        }
    }

    public Object getTarget(){
        return this.target;
    }

    public List<Object> getInterceptorsAndDynammicInterceptionAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {
        List<Object> cached = methodCache.get(method);
        if(null == cached){
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            methodCache.put(m, null);
        }
        return cached;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean pointCutMatch() {
        return this.pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }
}
