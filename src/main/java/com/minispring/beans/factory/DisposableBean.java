package com.minispring.beans.factory;

/**
 * Bean Destruction Interface
 * Beans implementing this interface will execute destroy method when container is closed
 * This is an important extension point in Spring lifecycle for resource cleanup
 */
public interface DisposableBean {
    
    /**
     * Called when bean is being destroyed
     * Can perform resource cleanup, connection closing, and other cleanup work in this method
     * 
     * @throws Exception exceptions that may be thrown during destruction
     */
    void destroy() throws Exception;
} 