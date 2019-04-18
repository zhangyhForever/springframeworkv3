package com.forever.springframework.webmvc.servlet;

import com.forever.springframework.annotation.GPRequestPram;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public class GPHandlerAdapter {

    public boolean supports(Object handler){
        return handler instanceof GPHandlerMapping;
    }

    public GPModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handle) throws Exception{
        GPHandlerMapping handlerMapping = (GPHandlerMapping) handle;
        Method method = handlerMapping.getMethod();

        //形参列表以及其在方法参数中的位置
        Map<String, Integer> paramMapping = new HashMap<String, Integer>();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for(int i=0; i<paramAnnotations.length; i++){
            for(Annotation paramAnnotation: paramAnnotations[i]){
                if(paramAnnotation.getClass() != GPRequestPram.class){
                    continue;
                }
                String paramName = ((GPRequestPram) paramAnnotation).value();
                if(!"".equals(paramName.trim()))
                paramMapping.put(paramName, i);
            }
        }

        //处理HttpServletRequset和HttpServletResponse
        Class<?>[] parameterTypes = method.getParameterTypes();
        for(int i=0; i<parameterTypes.length; i++){
            Class<?> type = parameterTypes[i];
            if(type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramMapping.put(type.getName(), i);
            }
        }

        //用户传过来的参数列表
        Map<String ,String[]> reqParameterMap = req.getParameterMap();
        Object[] paramValues = new Object[]{};
        //构造实参列表
        for(Map.Entry<String, String[]> reqParamEntity: reqParameterMap.entrySet()){
            String value = Arrays.toString(reqParamEntity.getValue())
                    .replaceAll("\\[|\\]","").replaceAll("\\s","");
            if(!paramMapping.containsKey(value)){
                continue;
            }
            Integer index = paramMapping.get(reqParamEntity.getKey());
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        if(paramMapping.containsKey(HttpServletRequest.class.getName())){
            Integer index = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if(paramMapping.containsKey(HttpServletResponse.class.getName())){
            Integer index = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = res;
        }

        Object controller = handlerMapping.getController();

        //获得方法返回值
        Object result = method.invoke(controller, paramValues);
        if(null == result){
            return null;
        }
        boolean isModelAndView = method.getReturnType() == GPModelAndView.class;
        if(isModelAndView){
            return (GPModelAndView)result;
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> parameterType) {
        if(String.class == parameterType){
            return value;
        }else if(Integer.class == parameterType){
            return Integer.valueOf(value);
        }else if(int.class == parameterType){
            return Integer.valueOf(value).intValue();
        }else {
            if(null != value){
                return value;
            }
            return null;
        }
    }
}
