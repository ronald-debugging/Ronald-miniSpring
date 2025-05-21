package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * Method before advice interface
 * Executes custom logic before target method execution
 */
public interface MethodBeforeAdvice extends BeforeAdvice {
    
    /**
     * Called before target method execution
     *
     * @param method The method being called
     * @param args The method arguments
     * @param target The target object
     * @throws Throwable Possible exceptions that may be thrown
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
} 