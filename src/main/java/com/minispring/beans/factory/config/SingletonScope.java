package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Scope Implementation
 * Default scope for beans where only one instance exists for the entire application
 */
public class SingletonScope implements Scope {
    
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, Runnable> destructionCallbacks = new HashMap<>(16);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // First try to get from cache
        Object bean = this.singletonObjects.get(name);
        if (bean == null) {
            try {
                // If not exists, create and cache it
                bean = objectFactory.getObject();
                this.singletonObjects.put(name, bean);
            } catch (BeansException ex) {
                throw ex;
            }
        }
        return bean;
    }
    
    @Override
    public Object remove(String name) {
        // Remove and return the singleton object
        Object bean = this.singletonObjects.remove(name);
        // Remove corresponding destruction callback
        this.destructionCallbacks.remove(name);
        return bean;
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        this.destructionCallbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "singleton";
    }
    
    /**
     * Execute all registered destruction callbacks
     * Called when application is shutting down
     */
    public void destroySingletons() {
        String[] singletonNames = this.destructionCallbacks.keySet().toArray(new String[0]);
        for (String name : singletonNames) {
            Runnable callback = this.destructionCallbacks.remove(name);
            if (callback != null) {
                try {
                    callback.run();
                }
                catch (Throwable ex) {
                    System.err.println("Exception thrown while executing destruction callback for singleton [" + name + "]: " + ex);
                }
            }
        }
        // Clear all singleton objects
        this.singletonObjects.clear();
    }
} 