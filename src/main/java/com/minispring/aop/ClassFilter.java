package com.minispring.aop;

/**
 * Class filter interface, used to determine which classes should be proxied
 */
public interface ClassFilter {
    
    /**
     * Determine if the given class matches
     * @param clazz The class to check
     * @return true if the class matches
     */
    boolean matches(Class<?> clazz);
    
    /**
     * Default class filter that matches all classes
     */
    ClassFilter TRUE = clazz -> true;
} 