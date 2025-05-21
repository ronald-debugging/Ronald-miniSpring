package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * Bean Factory Interface
 * Core interface of IoC container, defines methods for obtaining beans
 */
public interface BeanFactory {

    /**
     * Get bean instance by name
     * @param name bean name
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    Object getBean(String name) throws BeansException;

    /**
     * Get bean instance by name and type
     * @param name bean name
     * @param requiredType bean type
     * @param <T> bean type
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * Get bean instance by type
     * @param requiredType bean type
     * @param <T> bean type
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;
    
    /**
     * Get bean instance by name and constructor arguments
     * @param name bean name
     * @param args constructor arguments
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    Object getBean(String name, Object... args) throws BeansException;

    /**
     * Check if bean with specified name exists
     * @param name bean name
     * @return true if bean exists
     */
    boolean containsBean(String name);
} 