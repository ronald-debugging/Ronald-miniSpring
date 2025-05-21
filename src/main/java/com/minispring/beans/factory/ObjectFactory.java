package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * Object Factory Interface
 * Defines factory method for creating objects
 * Used for lazy initialization of objects
 * @param <T> 创建对象的类型
 */
public interface ObjectFactory<T> {
    
    /**
     * Get object instance
     * 
     * @return object instance
     * @throws BeansException If object creation fails
     */
    T getObject() throws BeansException;
} 