package com.forever.springframework.beans;

public interface GPBeanFactory {

    Object getBean(String beanName);

    Object getBean(Class<?> clazz);
}
