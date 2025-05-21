package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * Bean Instantiation Strategy Interface
 * Defines how to instantiate beans, allowing different instantiation mechanisms
 */
public interface InstantiationStrategy {

    /**
     * Instantiate a bean
     * @param beanDefinition bean definition
     * @param beanName bean name
     * @param ctor constructor to use, if null use default constructor
     * @param args constructor arguments, if null means no arguments (equivalent to empty array)
     * @return instantiated bean object
     * @throws BeansException if instantiation fails
     */
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException;
} 