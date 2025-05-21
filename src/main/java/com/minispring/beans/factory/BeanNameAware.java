package com.minispring.beans.factory;

/**
 * BeanNameAware Interface
 * Beans implementing this interface will be injected with their bean name during creation
 */
public interface BeanNameAware {
    
    /**
     * Set bean name
     * Called after bean property population and before initialization
     * 
     * @param name bean name
     */
    void setBeanName(String name);
} 