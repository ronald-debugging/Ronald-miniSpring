package com.minispring.beans.factory.support;
import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;

/**
 * Bean Definition Registry Interface
 * Defines methods for registering BeanDefinitions
 */
public interface BeanDefinitionRegistry {

    /**
     * Register BeanDefinition
     * @param beanName bean name
     * @param beanDefinition bean definition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * Get BeanDefinition
     * @param beanName bean name
     * @return bean definition
     * @throws BeansException if BeanDefinition not found
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * Check if contains BeanDefinition with specified name
     * @param beanName bean name
     * @return whether contains
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Get all registered bean names
     * @return array of bean names
     */
    String[] getBeanDefinitionNames();
} 