package com.forever.springframework.beans.support;

import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPService;
import com.forever.springframework.beans.config.GPBeanDefinition;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description: 读取配置文件的类
 * @Author: zhang
 * @Date: 2019/4/12
 */
@Slf4j
public class GPBeanDefinitionReader {

    private final String DEFAULT_PACKAGE = "package";

    private String[] configLocation;

    private Properties config = new Properties();

    private List<String> registerBeanClasses = new ArrayList<String>();

    public GPBeanDefinitionReader(String... configLocation){
        this.configLocation = configLocation;
    }

    public List<GPBeanDefinition> loadBeanDefinitions(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.configLocation[0].replace("classpath:", ""));
        try {
            config.load(is);
            //加载配置文件，拿到包名
            doScanner(config.getProperty(DEFAULT_PACKAGE));
            //扫描包下的文件，拿到所有的class文件类全名,并将其封装成BeanDefinition
            List<GPBeanDefinition> beanDefinitions = loadBeanDefinition();
            return beanDefinitions;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for(File file: classPath.listFiles()){
            if(file.isDirectory()){
                doScanner(packageName + "." + file.getName());
            }
            if(!file.getName().endsWith(".class")){
                continue;
            }
            String className = (packageName + "." + file.getName().replace(".class",""));
            registerBeanClasses.add(className);
        }
        log.info("=======doScanner完成==========");
    }

    public List<GPBeanDefinition> loadBeanDefinition(){
        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition>();
        for(String className: registerBeanClasses){
            GPBeanDefinition beanDefinition = doLoadBeanDefinition(className);
            if(beanDefinition == null){
                continue;
            }
            result.add(beanDefinition);
        }
        log.info("============loadBeanDefinition完成===========");
        return result;
    }

    private GPBeanDefinition doLoadBeanDefinition(String className) {
        try{
            Class<?> clazz = Class.forName(className);
            if(!clazz.isInterface()
                    && (clazz.isAnnotationPresent(GPController.class)
                    || clazz.isAnnotationPresent(GPService.class))){
                GPBeanDefinition beanDefinition = new GPBeanDefinition();
                beanDefinition.setBeanClassName(className);
                beanDefinition.setFactoryBeanName(toLowerFirstCase(clazz.getSimpleName()));
                log.info(beanDefinition.getBeanClassName()+" 注册名====>"+beanDefinition.getFactoryBeanName());
                return beanDefinition;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Properties getConfig(){
        return this.config;
    }

    private String toLowerFirstCase(String classSimpleName) {
        char[] charArray = classSimpleName.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}
