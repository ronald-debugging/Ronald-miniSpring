package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

/**
 * Bean factory post-processor interface
 * Allows customization of application context's bean definitions and adjustment of context's bean property values
 * Called after all bean definitions are loaded but before bean instantiation
 */
public interface BeanFactoryPostProcessor {
    
    /**
     * Modify bean definitions in bean factory before bean instantiation
     * @param beanFactory configurable bean factory
     * @throws BeansException exceptions during processing
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
} 