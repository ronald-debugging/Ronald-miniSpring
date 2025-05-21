package com.minispring.beans.factory;

/**
 * Hierarchical Bean Factory Interface
 * Defines BeanFactory with parent-child relationship
 */
public interface HierarchicalBeanFactory extends BeanFactory {
    
    /**
     * Get parent BeanFactory
     * 
     * @return parent BeanFactory, returns null if none exists
     */
    BeanFactory getParentBeanFactory();
    
    /**
     * Check if local BeanFactory contains bean with specified name
     * 
     * @param name bean name
     * @return true if contained locally
     */
    boolean containsLocalBean(String name);
} 