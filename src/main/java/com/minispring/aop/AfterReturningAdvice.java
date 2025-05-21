package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * Method after-returning advice interface
 * Executes custom logic after successful execution of the target method
 */
public interface AfterReturningAdvice extends AfterAdvice {
    
    /**
     * Called after successful execution of the target method
     *
     * @param returnValue The value returned by the method, if any
     * @param method The executed method
     * @param args The method arguments
     * @param target The target object
     * @throws Throwable Possible exceptions that may be thrown
     */
    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
} 