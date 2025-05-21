package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;

import java.util.Set;

/**
 * Configurable Listable Bean Factory Interface
 * Provides methods for configuring BeanFactory
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
    
    /**
     * Get BeanDefinition by name
     * 
     * @param beanName bean name
     * @return bean definition
     * @throws BeansException if bean definition not found
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    
    /**
     * Pre-instantiate all singleton beans
     * 
     * @throws BeansException if an error occurs during initialization
     */
    void preInstantiateSingletons() throws BeansException;
    
    /**
     * Get all bean definition names
     * @return array of bean definition names
     */
    @Override
    String[] getBeanDefinitionNames();
    
    /**
     * Add BeanPostProcessor
     * 
     * @param beanPostProcessor processor to add
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
} 