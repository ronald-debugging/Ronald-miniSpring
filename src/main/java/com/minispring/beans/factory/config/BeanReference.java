package com.minispring.beans.factory.config;

/**
 * Bean Reference
 * Used to represent references to other beans, used in property injection and constructor injection
 * Provides basic support for resolving circular dependencies
 */
public class BeanReference {

    private final String beanName;

    /**
     * Create a Bean reference
     * 
     * @param beanName name of the referenced bean
     */
    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    /**
     * Get the name of the referenced bean
     * 
     * @return bean name
     */
    public String getBeanName() {
        return this.beanName;
    }
    
    @Override
    public String toString() {
        return "Reference to Bean[" + this.beanName + "]";
    }
} 