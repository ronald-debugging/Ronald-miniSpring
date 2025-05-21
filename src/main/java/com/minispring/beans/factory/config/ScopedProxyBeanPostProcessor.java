package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableBeanFactory;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

/**
 * Scoped Proxy Bean Post Processor
 * Used to create proxies for beans that require scope proxying
 */
public class ScopedProxyBeanPostProcessor implements BeanPostProcessor {
    
    // The owning bean factory
    private final ConfigurableBeanFactory beanFactory;
    
    public ScopedProxyBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Check if bean definition exists
        BeanDefinition beanDefinition = null;
        try {
            // If it's a ConfigurableListableBeanFactory, get the bean definition
            if (beanFactory instanceof ConfigurableListableBeanFactory) {
                ConfigurableListableBeanFactory listableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
                beanDefinition = listableBeanFactory.getBeanDefinition(beanName);
            }
        } catch (BeansException e) {
            // If bean definition not found, return original bean
            return bean;
        }
        
        // If no bean definition found or scoped proxy not needed, return original bean
        if (beanDefinition == null || !beanDefinition.isScopedProxy()) {
            return bean;
        }
        
        // Get scope name
        String scopeName = beanDefinition.getScope();
        if (scopeName == null || scopeName.equals(ConfigurableBeanFactory.SCOPE_SINGLETON) 
                || scopeName.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)) {
            // Singleton and prototype scopes don't need proxying
            return bean;
        }
        
        // Create scoped proxy
        return ScopedProxyFactory.createScopedProxy(bean, beanName, scopeName, (ConfigurableBeanFactory)beanFactory);
    }
} 