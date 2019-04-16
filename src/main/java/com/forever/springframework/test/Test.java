package com.forever.springframework.test;

import com.forever.springframework.test.controller.ActionController;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public class Test {

    public static void main(String[] args) {
       /* GPBeanDefinitionReader reader = new GPBeanDefinitionReader("application.properties");
        URL url = reader.getClass().getResource("/" + "com.forever.com.forever.springframework".replaceAll("\\.","/"));
        System.out.println(url.getPath());
        GPApplicationContext context = new GPApplicationContext("classpath:application.properties");
        Object bean = context.getBean(ActionController.class);
        System.out.println(bean);*/
       /* Field[] fields = ActionController.class.getDeclaredFields();
        System.out.println("hahahaaha");
        for(Field field: fields){
            System.out.println(field.getType());
        }*/

        System.out.println(Test.class.getClassLoader().getResource(""));
    }
}
