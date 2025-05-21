package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Simple Instantiation Strategy
 * Bean instantiation strategy implemented using JDK reflection mechanism
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            if (ctor != null) {
                // Parameter validation
                if (args == null) {
                    args = new Object[0];
                }
                
                // Check if parameter count matches
                if (args.length != ctor.getParameterCount()) {
                    throw new BeansException("Constructor parameter count mismatch: " + beanName + 
                            ", expected " + ctor.getParameterCount() + " parameters but got " + args.length + " parameters");
                }
                
                // Instantiate using specified constructor
                return ctor.newInstance(args);
            } else {
                // Instantiate using default constructor
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate bean [" + beanName + "]", e);
        }
    }
} 