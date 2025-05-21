package com.minispring.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Parameter name discovery interface
 * Used to obtain parameter names of methods and constructors
 */
public interface ParameterNameDiscoverer {
    
    /**
     * Get parameter names of a method
     * @param method method
     * @return array of parameter names, returns null if cannot be obtained
     */
    String[] getParameterNames(Method method);
    
    /**
     * Get parameter names of a constructor
     * @param constructor constructor
     * @return array of parameter names, returns null if cannot be obtained
     */
    String[] getParameterNames(Constructor<?> constructor);
} 