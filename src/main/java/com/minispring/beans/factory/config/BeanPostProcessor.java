package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;

/**
 * Bean post-processor interface
 * Allows customization of new bean instances, such as checking marker interfaces or wrapping beans with proxies
 * This is the foundation for Spring AOP, transactions, and other features
 */
public interface BeanPostProcessor {
    
    /**
     * Apply this BeanPostProcessor before bean initialization
     * @param bean the original bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException exceptions during processing
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    /**
     * Apply this BeanPostProcessor after bean initialization
     * @param bean the original bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException exceptions during processing
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
} 