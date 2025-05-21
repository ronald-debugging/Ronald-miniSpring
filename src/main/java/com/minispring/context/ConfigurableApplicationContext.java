package com.minispring.context;

import com.minispring.beans.BeansException;
import com.minispring.core.env.ConfigurableEnvironment;

/**
 * Configurable ApplicationContext Interface
 * Provides methods for configuring application context
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
    
    /**
     * Refresh application context
     * 
     * @throws BeansException if an error occurs during refresh
     */
    void refresh() throws BeansException;
    
    /**
     * Close application context
     */
    void close();
    
    /**
     * Publish application event
     * 
     * @param event event to publish
     */
    void publishEvent(ApplicationEvent event);
    
    /**
     * Get Environment
     * 
     * @return configurable Environment
     */
    ConfigurableEnvironment getEnvironment();
    
    /**
     * Set Environment
     * 
     * @param environment configurable Environment
     */
    void setEnvironment(ConfigurableEnvironment environment);
} 