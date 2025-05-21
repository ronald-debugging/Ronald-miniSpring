package com.minispring.beans.factory.config;

import com.minispring.beans.factory.ObjectFactory;

/**
 * Bean Scope Interface
 * Defines bean scope behavior and lifecycle
 */
public interface Scope {
    
    /**
     * Get object from scope
     * @param name bean name
     * @param objectFactory factory to create bean object if it doesn't exist
     * @return bean instance from the scope
     */
    Object get(String name, ObjectFactory<?> objectFactory);
    
    /**
     * Remove object from scope
     * @param name bean name
     * @return removed object, or null if not found
     */
    Object remove(String name);
    
    /**
     * Register destruction callback
     * This callback will be called when the scope ends
     * @param name bean name
     * @param callback destruction callback
     */
    void registerDestructionCallback(String name, Runnable callback);
    
    /**
     * Get conversation ID for this scope
     * Can be used to distinguish different scope instances
     * @return conversation ID, or null if none
     */
    String getConversationId();
} 