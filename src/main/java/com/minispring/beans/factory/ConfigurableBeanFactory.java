package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.beans.factory.config.Scope;

/**
 * Configurable Bean Factory Interface
 * Provides methods for configuring BeanFactory
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
    
    /**
     * Singleton scope
     */
    String SCOPE_SINGLETON = "singleton";
    
    /**
     * Prototype scope
     */
    String SCOPE_PROTOTYPE = "prototype";
    
    /**
     * Register scope
     * @param scopeName scope name
     * @param scope scope implementation
     */
    void registerScope(String scopeName, Scope scope);
    
    /**
     * Get registered scope
     * @param scopeName scope name
     * @return scope implementation, returns null if not found
     */
    Scope getRegisteredScope(String scopeName);
    
    /**
     * Get bean type
     * @param name bean name
     * @return bean type
     * @throws BeansException if type cannot be determined
     */
    Class<?> getType(String name) throws BeansException;
    
    /**
     * Add BeanPostProcessor
     * 
     * @param beanPostProcessor processor to add
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    
    /**
     * Destroy singleton beans
     */
    void destroySingletons();
} 