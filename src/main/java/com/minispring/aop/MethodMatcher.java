package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * Method matcher interface, used to determine which methods should be proxied
 */
public interface MethodMatcher {
    
    /**
     * Determine if the given method matches
     * @param method The method to check
     * @param targetClass The target class
     * @return true if the method matches
     */
    boolean matches(Method method, Class<?> targetClass);
    
    /**
     * Determine if this is a dynamic method matcher
     * Static matching: Method matching can be determined at proxy creation time
     * Dynamic matching: Method matching needs to be checked at each method invocation (parameter values may affect matching)
     * @return true if this is a dynamic matcher
     */
    boolean isRuntime();
    
    /**
     * Check if the given method and arguments match (only used for runtime dynamic matching)
     * @param method The method to check
     * @param targetClass The target class
     * @param args The method arguments
     * @return true if the method matches
     */
    boolean matches(Method method, Class<?> targetClass, Object... args);
    
    /**
     * Default method matcher that matches all methods
     */
    MethodMatcher TRUE = new MethodMatcher() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return true;
        }
        
        @Override
        public boolean isRuntime() {
            return false;
        }
        
        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return true;
        }
    };
} 