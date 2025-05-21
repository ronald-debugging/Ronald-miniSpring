package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Scope Implementation
 * Creates a new instance each time a bean is requested
 */
public class PrototypeScope implements Scope {
    
    // For storing destruction callbacks
    private final Map<String, Runnable> destructionCallbacks = new HashMap<>(16);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        try {
            // In prototype mode, create a new object each time
            return objectFactory.getObject();
        } catch (BeansException ex) {
            throw ex;
        }
    }
    
    @Override
    public Object remove(String name) {
        // Remove from callback collection
        this.destructionCallbacks.remove(name);
        return null; // Return null since objects aren't cached in prototype mode
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // Register destruction callback
        this.destructionCallbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "prototype";
    }
    
    /**
     * Execute and clear all destruction callbacks
     * Note: In prototype mode, lifecycle is typically managed by the client
     * This method is usually not called as prototype bean lifecycle is not managed by the container
     */
    public void destroyPrototypes() {
        String[] prototypeNames = this.destructionCallbacks.keySet().toArray(new String[0]);
        for (String name : prototypeNames) {
            Runnable callback = this.destructionCallbacks.remove(name);
            if (callback != null) {
                try {
                    callback.run();
                }
                catch (Throwable ex) {
                    System.err.println("Exception thrown while executing destruction callback for prototype [" + name + "]: " + ex);
                }
            }
        }
    }
} 