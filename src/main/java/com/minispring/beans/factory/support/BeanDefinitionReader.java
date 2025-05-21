package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.core.io.Resource;
import com.minispring.core.io.ResourceLoader;

/**
 * Bean Definition Reader Interface
 * Defines methods for reading bean definitions from different sources
 */
public interface BeanDefinitionReader {

    /**
     * Get bean definition registry
     * @return bean definition registry
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * Get resource loader
     * @return resource loader
     */
    ResourceLoader getResourceLoader();

    /**
     * Load bean definitions from resource
     * @param resource resource
     * @throws BeansException if loading fails
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * Load bean definitions from multiple resources
     * @param resources resource array
     * @throws BeansException if loading fails
     */
    void loadBeanDefinitions(Resource... resources) throws BeansException;

    /**
     * Load bean definitions from specified location
     * @param location resource location
     * @throws BeansException if loading fails
     */
    void loadBeanDefinitions(String location) throws BeansException;

    /**
     * Load bean definitions from multiple locations
     * @param locations resource location array
     * @throws BeansException if loading fails
     */
    void loadBeanDefinitions(String... locations) throws BeansException;
} 