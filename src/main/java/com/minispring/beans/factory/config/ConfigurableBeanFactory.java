package com.minispring.beans.factory.config;

import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.BeansException;

/**
 * Configurable Bean Factory Interface
 * Extends BeanFactory interface with additional configuration capabilities
 */
public interface ConfigurableBeanFactory extends BeanFactory {
    
    /**
     * Constant identifier for singleton scope
     */
    String SCOPE_SINGLETON = "singleton";
    
    /**
     * Constant identifier for prototype scope
     */
    String SCOPE_PROTOTYPE = "prototype";
    
    /**
     * Register a scope
     * @param scopeName name of the scope
     * @param scope scope object
     */
    void registerScope(String scopeName, Scope scope);
    
    /**
     * Get a registered scope
     * @param scopeName name of the scope
     * @return scope object, or null if not found
     */
    Scope getRegisteredScope(String scopeName);
    
    /**
     * Get the type of a bean
     * @param name name of the bean
     * @return type of the bean
     * @throws BeansException if type cannot be determined
     */
    Class<?> getType(String name) throws BeansException;
    
    /**
     * Set the parent bean factory
     * @param parentBeanFactory parent bean factory
     */
    void setParentBeanFactory(BeanFactory parentBeanFactory);
    
    /**
     * Add a bean post processor
     * @param beanPostProcessor bean post processor to add
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    
    /**
     * Destroy all singleton beans
     * Called when container is shutting down
     */
    void destroySingletons();
} 