package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.core.io.FileSystemResource;
import com.minispring.core.io.Resource;

/**
 * File System XML Application Context
 * Loads XML configuration files from file system
 */
public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {
    
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
    public FileSystemXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }
    
    /**
     * Constructor with multiple configuration files
     * 
     * @param configLocations array of configuration file locations
     * @throws BeansException if context creation fails
     */
    public FileSystemXmlApplicationContext(String[] configLocations) throws BeansException {
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
     * Get resource
     * Load resource from file system
     * 
     * @param location resource location
     * @return resource object
     */
    @Override
    public Resource getResource(String location) {
        return new FileSystemResource(location);
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