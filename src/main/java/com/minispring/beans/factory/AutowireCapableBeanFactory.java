package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;

/**
 * Autowire Capable Bean Factory Interface
 * Provides methods for autowiring beans
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    
    /**
     * Autowire by type
     */
    int AUTOWIRE_BY_TYPE = 1;
    
    /**
     * Autowire by name
     */
    int AUTOWIRE_BY_NAME = 2;
    
    /**
     * Autowire by constructor
     */
    int AUTOWIRE_CONSTRUCTOR = 3;
    
    /**
     * No autowiring
     */
    int AUTOWIRE_NO = 0;
    
    /**
     * Create a new bean instance
     * 
     * @param beanClass bean class
     * @return bean instance
     * @throws BeansException if an error occurs during creation
     */
    Object createBean(Class<?> beanClass) throws BeansException;
    
    /**
     * Autowire an existing bean instance
     * 
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @throws BeansException if an error occurs during autowiring
     */
    void autowireBean(Object existingBean, String beanName) throws BeansException;
    
    /**
     * Apply BeanPostProcessor's before initialization
     * 
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean
     * @throws BeansException if an error occurs during processing
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    
    /**
     * Apply BeanPostProcessor's after initialization
     * 
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean
     * @throws BeansException if an error occurs during processing
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}