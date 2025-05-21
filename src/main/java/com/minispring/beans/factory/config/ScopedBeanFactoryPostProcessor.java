package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

import java.util.Arrays;

/**
 * Scoped Bean Factory Post Processor
 * Responsible for processing scope information in bean definitions and creating corresponding proxies
 */
public class ScopedBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Get names of all bean definitions
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String scope = beanDefinition.getScope();
            
            // Process beans that are neither singleton nor prototype
            if (scope != null && !scope.equals(ConfigurableBeanFactory.SCOPE_SINGLETON) 
                    && !scope.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)) {
                
                // Check if scope proxy is needed
                if (beanDefinition.isScopedProxy()) {
                    // The logic here is: create proxies for scoped beans and replace the actual object in bean definition with the proxy
                    // This way, when other beans reference this scoped bean, they actually reference the proxy
                    // The proxy will get the actual object from the scope on each method call
                    beanDefinition.setScopedProxy(true);
                    
                    // Here, we don't need to create the proxy immediately since the actual bean hasn't been created yet
                    // We just need to mark it as needing a proxy, then handle it during bean creation process
                    
                    // Notify the bean definition that it needs a proxy creator
                    beanDefinition.setAttribute("scopedProxyBeanName", beanName);
                    beanDefinition.setAttribute("originalScope", scope);
                }
            }
        }
    }
} 