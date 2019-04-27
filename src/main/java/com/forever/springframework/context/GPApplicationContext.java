package com.forever.springframework.context;

import com.forever.springframework.annotation.GPAutowired;
import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPService;
import com.forever.springframework.aop.GPAopProxy;
import com.forever.springframework.aop.GPCglibAopProxy;
import com.forever.springframework.aop.GPJdkDynamicAopProxy;
import com.forever.springframework.aop.config.GPAopConfig;
import com.forever.springframework.aop.support.GPAdvisedSupport;
import com.forever.springframework.beans.GPBeanFactory;
import com.forever.springframework.beans.GPBeanWrapper;
import com.forever.springframework.beans.config.GPBeanDefinition;
import com.forever.springframework.beans.config.GPBeanPostProcessor;
import com.forever.springframework.beans.support.GPBeanDefinitionReader;
import com.forever.springframework.beans.support.GPDefaultListableBeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    private String[] configLocations;

    private GPBeanDefinitionReader reader;

    private Map<String, Object> singletonBeanCacheMap = new HashMap<String, Object>();

    private Map<String, GPBeanWrapper> beanWrapperMap = new HashMap<String, GPBeanWrapper>();

    public GPApplicationContext(String... configLocations){
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    public void refresh() {
        //1、定位配置文件
        reader = new GPBeanDefinitionReader(this.configLocations);

        //2、加载配置文件，扫描相关的类，把它们封装成beanDefinition
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、注册，把配置信息放到容器里面（伪IOC容器）
        doRegisterBeanDefinition(beanDefinitions);

        //4、把不是懒加载的类提前初始化
        doCreateBean();

        //5、将初始化过的bean进行依赖注入
        doAutowired();
  }

    private void doCreateBean() {
        for(Map.Entry<String, GPBeanDefinition> beanDefinitionEntry: super.beanDefinitionMap.entrySet()){
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                getBean(beanDefinitionEntry.getKey());
            }
        }
    }

    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) {
        for(GPBeanDefinition beanDefinition: beanDefinitions){
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    private void doAutowired() {
        for(Map.Entry<String, GPBeanWrapper> beanWrapperEntry: this.beanWrapperMap.entrySet()){
            populateBean(beanWrapperEntry.getValue());
        }
    }

    public Object getBean(String beanName) {
        //1、初始化
        //根据beanName获得bean的信息
        GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        //生成通知事件
        GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
        //实例化bean
        Object instance = instantiateBean(beanDefinition);
        if(null == instance){
            return null;
        }
        //实例初始化前调用一次
        beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
        //封装到beanWrapper中
        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
        this.beanWrapperMap.put(beanName, beanWrapper);
        //实例化后调用一次
        beanPostProcessor.postProcessAfterInitialization(instance, beanName);
        //添加前置后置操作，为我们自己留下可操作的空间
        return this.beanWrapperMap.get(beanName).getWrapperInstance();
    }

    public Object getBean(Class<?> clazz) {
        return getBean(toLowerFirstCase(clazz.getSimpleName()));
    }

    private Object instantiateBean(GPBeanDefinition beanDefinition) {
        String beanName = beanDefinition.getFactoryBeanName();
        String className = beanDefinition.getBeanClassName();
        try{
            Object instance = null;
            //单例直接取出对象
            if(this.singletonBeanCacheMap.containsKey(beanName)){
                instance = this.singletonBeanCacheMap.get(beanName);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                //有aop配置的类在此进行配置
                GPAdvisedSupport config = instantionAopConfig(beanDefinition);
                if(config.pointCutMatch()){
                    instance = createProxy(config).getProxy();
                }
                config.setTargetClass(clazz);
                config.setTarget(instance);

                this.singletonBeanCacheMap.put(beanName, instance);
                log.info(this.getClass()+"创建对象"+instance);
            }
            return instance;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private GPAopProxy createProxy(GPAdvisedSupport config) {
        Class<?> targetClass = config.getTargetClass();
        Class<?>[] interfaces = targetClass.getInterfaces();
        if(interfaces.length > 0) {
            return new GPJdkDynamicAopProxy(config);
        }
        return new GPCglibAopProxy(config);
    }

    /**
     * 获取aop的配置信息,并返回配置信息的包装类
     * @param beanDefinition
     * @return
     */
    private GPAdvisedSupport instantionAopConfig(GPBeanDefinition beanDefinition) {
        GPAopConfig config = new GPAopConfig();
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new GPAdvisedSupport(config);
    }

    private void populateBean(GPBeanWrapper beanWrapper) {
        Class<?> wrapperClass = beanWrapper.getWrapperClass();
        if(!wrapperClass.isAnnotationPresent(GPController.class) && !wrapperClass.isAnnotationPresent(GPService.class)){
            return;
        }
        Field[] fields = wrapperClass.getDeclaredFields();
        for(Field field: fields){
            if(!field.isAnnotationPresent(GPAutowired.class)){
                return;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            String beanName = autowired.value().trim();
            if("".equals(beanName)){
                beanName = field.getName();
            }
            GPBeanWrapper autowiredInstance = this.beanWrapperMap.get(beanName);
            if(null == autowiredInstance){
                return;
            }
            field.setAccessible(true);
            try {
                field.set(beanWrapper.getWrapperInstance(), autowiredInstance.getWrapperInstance());
                log.info(beanWrapper.getWrapperClass()+"注入实例"+ autowiredInstance.getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }

    public String[] getBeanDefinitionNames(){
        return beanWrapperMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    private String toLowerFirstCase(String classSimpleName) {
        char[] charArray = classSimpleName.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

}
