package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Bean Factory
 * Implements BeanFactory interface, extends DefaultSingletonBeanRegistry
 * Provides template method for getting beans
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /** List of BeanPostProcessors */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) doGetBean(name, requiredType, null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBean(requiredType.getName(), requiredType);
    }
    
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, null, args);
    }

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name) || containsBeanDefinition(name);
    }
    
    /**
     * Add BeanPostProcessor
     * @param beanPostProcessor bean post processor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // Avoid duplicate addition
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }
    
    /**
     * Get list of BeanPostProcessors
     * @return list of bean post processors
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
    
    /**
     * Execute BeanPostProcessor pre-processing
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean instance
     */
    public abstract Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    
    /**
     * Execute BeanPostProcessor post-processing
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean instance
     */
    public abstract Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * Actual implementation of getting bean
     * @param name bean name
     * @param requiredType bean type
     * @param args constructor arguments
     * @param <T> bean type
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args) throws BeansException {
        // First try to get from singleton bean cache
        Object bean = getSingleton(name);
        if (bean != null) {
            System.out.println("Retrieved bean from cache: " + name);
            return (T) bean;
        }

        // If not found, create bean instance
        BeanDefinition beanDefinition = getBeanDefinition(name);
        
        if (beanDefinition.isSingleton()) {
            // For singleton beans, use getSingleton method to create and cache
            bean = getSingleton(name, new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    try {
                        return createBean(name, beanDefinition, args);
                    } catch (Exception e) {
                        throw new BeansException("Failed to create bean [" + name + "]", e);
                    }
                }
            });
            System.out.println("Created and cached singleton bean: " + name);
        } else {
            // For prototype beans, create new instance directly
            bean = createBean(name, beanDefinition, args);
            System.out.println("Created prototype bean: " + name);
        }
        
        return (T) bean;
    }

    /**
     * Check if contains BeanDefinition with specified name
     * @param beanName bean name
     * @return whether contains
     */
    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * Get BeanDefinition
     * @param beanName bean name
     * @return bean definition
     * @throws BeansException if BeanDefinition not found
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * Create bean instance
     * @param beanName bean name
     * @param beanDefinition bean definition
     * @param args constructor arguments
     * @return bean instance
     * @throws BeansException if bean creation fails
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
} 