package com.minispring.aop.framework;

import java.lang.reflect.Method;

/**
 * Abstract interface for method invocation
 * Represents a runtime method invocation
 */
public interface MethodInvocation {
    
    /**
     * Get the invoked method
     * @return method object
     */
    Method getMethod();
    
    /**
     * Get method arguments
     * @return array of arguments
     */
    Object[] getArguments();
    
    /**
     * Get target object
     * @return target object
     */
    Object getThis();
    
    /**
     * Continue the method invocation chain
     * @return return value of the method invocation
     * @throws Throwable exceptions that may be thrown by method invocation
     */
    Object proceed() throws Throwable;
} 