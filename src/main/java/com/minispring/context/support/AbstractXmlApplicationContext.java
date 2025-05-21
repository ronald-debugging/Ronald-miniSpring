package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.Resource;

/**
 * Abstract implementation of XML-based application context
 * Loads Bean definitions from XML files
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
    
    /**
     * Load Bean definitions
     * Load Bean definitions from XML configuration files
     * 
     * @param beanFactory Bean factory
     * @throws BeansException if an error occurs during loading
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        // Create XmlBeanDefinitionReader
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        
        // Get configuration file resources
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            // Load Bean definitions from resources
            beanDefinitionReader.loadBeanDefinitions(configResources);
        }
        
        // Get configuration file locations
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            // Load Bean definitions from locations
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }
    
    /**
     * Get configuration resources
     * Default implementation returns null, subclasses can override
     * 
     * @return array of configuration resources
     */
    protected Resource[] getConfigResources() {
        return null;
    }
    
    /**
     * Get configuration file locations
     * Implemented by subclasses, returns configuration file locations
     * 
     * @return array of configuration file locations
     */
    protected abstract String[] getConfigLocations();
} 