package com.minispring.beans.factory.config;

/**
 * Singleton Bean Registry Interface
 * Defines methods for managing singleton beans
 */
public interface SingletonBeanRegistry {

    /**
     * Get a singleton bean
     * @param beanName name of the bean
     * @return bean instance
     */
    Object getSingleton(String beanName);

    /**
     * Register a singleton bean
     * @param beanName name of the bean
     * @param singletonObject bean instance
     */
    void registerSingleton(String beanName, Object singletonObject);
} 