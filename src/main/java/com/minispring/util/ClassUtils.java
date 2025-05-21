package com.minispring.util;

/**
 * Class utility, provides utility methods related to class loading
 */
public class ClassUtils {
    
    /**
     * Get default class loader
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            // First, try to get the current thread's context class loader
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context class loader, ignore
        }
        if (cl == null) {
            // Second, use the class loader of ClassUtils class
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // Finally, use the system class loader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot get system class loader, ignore
                }
            }
        }
        return cl;
    }
    
    /**
     * Check if the given class exists
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
} 