package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * BeanFactoryAware Interface
 * Beans implementing this interface will be injected with BeanFactory during creation
 */
public interface BeanFactoryAware {
    
    /**
     * Set BeanFactory
     * Called after bean property population and before initialization
     * 
     * @param beanFactory the owning BeanFactory
     * @throws BeansException if an error occurs during setting
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
} 