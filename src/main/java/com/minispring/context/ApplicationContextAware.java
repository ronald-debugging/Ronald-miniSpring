package com.minispring.context;

import com.minispring.beans.BeansException;

/**
 * ApplicationContextAware Interface
 * Beans implementing this interface will have ApplicationContext injected during creation
 */
public interface ApplicationContextAware {
    
    /**
     * Set ApplicationContext
     * Called after bean property population and before initialization
     * 
     * @param applicationContext the ApplicationContext that the bean belongs to
     * @throws BeansException if an error occurs during setting
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
} 