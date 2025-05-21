package com.minispring.aop.framework;

/**
 * Core interface for AOP proxy
 * Defines methods for obtaining proxy objects
 */
public interface AopProxy {
    
    /**
     * Create a new proxy object
     * @return proxy object
     */
    Object getProxy();
    
    /**
     * Create a new proxy object using the given class loader
     * @param classLoader class loader used to create the proxy
     * @return proxy object
     */
    Object getProxy(ClassLoader classLoader);
} 