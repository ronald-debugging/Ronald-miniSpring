package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationContextAware;

/**
 * ApplicationContextAware Processor
 * Processes beans that implement ApplicationContextAware interface
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    
    private final ApplicationContext applicationContext;
    
    /**
     * Constructor
     * 
     * @param applicationContext application context
     */
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    /**
     * Process before bean initialization
     * 
     * @param bean bean instance
     * @param beanName bean name
     * @return processed bean
     * @throws BeansException if an error occurs during processing
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }
    
    /**
     * Process after bean initialization
     * 
     * @param bean bean instance
     * @param beanName bean name
     * @return processed bean
     * @throws BeansException if an error occurs during processing
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
} 