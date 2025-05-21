package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

import java.util.Map;

/**
 * Listable Bean Factory Interface
 * Extends BeanFactory to provide enumeration and search capabilities for beans
 */
public interface ListableBeanFactory extends BeanFactory {
    
    /**
     * Check if BeanFactory contains bean with specified name
     * 
     * @param name bean name
     * @return true if contained
     */
    boolean containsBeanDefinition(String name);
    
    /**
     * Get all registered bean names
     * 
     * @return array of bean names
     */
    String[] getBeanDefinitionNames();
    
    /**
     * Get all beans of specified type
     * 
     * @param type bean type
     * @return Map of bean names to bean instances
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);
} 