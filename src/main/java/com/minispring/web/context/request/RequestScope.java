package com.minispring.web.context.request;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP request scope implementation
 * Bean lifecycle is the same as HTTP request, one Bean instance is shared within a request
 */
public class RequestScope implements Scope {
    
    private final ThreadLocal<Map<String, Object>> requestScope = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, Runnable>> destructionCallbacks = ThreadLocal.withInitial(HashMap::new);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = this.requestScope.get();
        Object bean = scope.get(name);
        
        if (bean == null) {
            try {
                bean = objectFactory.getObject();
                scope.put(name, bean);
            } catch (BeansException ex) {
                throw ex;
            }
        }
        
        return bean;
    }
    
    @Override
    public Object remove(String name) {
        Map<String, Object> scope = this.requestScope.get();
        return scope.remove(name);
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        Map<String, Runnable> callbacks = this.destructionCallbacks.get();
        callbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "request-" + Thread.currentThread().getName();
    }
    
    /**
     * Execute request end callback
     * Usually called when HTTP request ends
     */
    public void endRequest() {
        Map<String, Runnable> callbacks = this.destructionCallbacks.get();
        
        for (Map.Entry<String, Runnable> entry : callbacks.entrySet()) {
            try {
                entry.getValue().run();
            }
            catch (Throwable ex) {
                System.err.println("Exception thrown while executing destruction callback for request bean [" + entry.getKey() + "]: " + ex);
            }
        }
        
        // Clean up ThreadLocal resources
        this.requestScope.remove();
        this.destructionCallbacks.remove();
    }
} 