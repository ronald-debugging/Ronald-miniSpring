package com.minispring.test.processor;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;

/**
 * Custom BeanPostProcessor
 * Used for testing BeanPostProcessor functionality
 */
public class CustomBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor pre-processing: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor post-processing: " + beanName);
        return bean;
    }
} 