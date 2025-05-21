package com.minispring.context.support;

import com.minispring.beans.BeansException;

/**
 * Classpath XML Application Context
 * Loads XML configuration files from classpath
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    
    /**
     * Configuration file locations
     */
    private String[] configLocations;
    
    /**
     * Constructor with single configuration file
     * 
     * @param configLocation configuration file location
     * @throws BeansException if context creation fails
     */
    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }
    
    /**
     * Constructor with multiple configuration files
     * 
     * @param configLocations array of configuration file locations
     * @throws BeansException if context creation fails
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }
    
    /**
     * Get configuration file locations
     * 
     * @return array of configuration file locations
     */
    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }
    
    /**
     * Check if contains Bean definition with specified name
     * 
     * @param beanName bean name
     * @return true if contains
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }
}