package com.minispring.web.context.request;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP session scope implementation
 * Bean lifecycle is the same as HTTP session, one Bean instance is shared within a session
 */
public class SessionScope implements Scope {
    
    // Use sessionId as key, corresponding Bean instance collection as value
    private final Map<String, Map<String, Object>> sessionBeanMap = new ConcurrentHashMap<>(16);
    // Destruction callback mapping
    private final Map<String, Map<String, Runnable>> destructionCallbacks = new ConcurrentHashMap<>(16);
    
    // Interface for getting current session ID, can be set externally
    private SessionIdResolver sessionIdResolver = () -> "default-session";
    
    /**
     * Set session ID resolver
     * @param sessionIdResolver session ID resolver
     */
    public void setSessionIdResolver(SessionIdResolver sessionIdResolver) {
        this.sessionIdResolver = sessionIdResolver;
    }
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        String sessionId = sessionIdResolver.resolveSessionId();
        Map<String, Object> sessionMap = this.sessionBeanMap.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>(8));
        
        Object bean = sessionMap.get(name);
        if (bean == null) {
            try {
                bean = objectFactory.getObject();
                sessionMap.put(name, bean);
            } catch (BeansException ex) {
                throw ex;
            }
        }
        
        return bean;
    }
    
    @Override
    public Object remove(String name) {
        String sessionId = sessionIdResolver.resolveSessionId();
        Map<String, Object> sessionMap = this.sessionBeanMap.get(sessionId);
        if (sessionMap != null) {
            return sessionMap.remove(name);
        }
        return null;
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        String sessionId = sessionIdResolver.resolveSessionId();
        Map<String, Runnable> callbacks = this.destructionCallbacks.computeIfAbsent(sessionId, k -> new HashMap<>(8));
        callbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "session-" + sessionIdResolver.resolveSessionId();
    }
    
    /**
     * Execute session end callback
     * Usually called when HTTP session ends
     * @param sessionId session ID
     */
    public void endSession(String sessionId) {
        // Execute destruction callbacks
        Map<String, Runnable> callbacks = this.destructionCallbacks.remove(sessionId);
        if (callbacks != null) {
            for (Map.Entry<String, Runnable> entry : callbacks.entrySet()) {
                try {
                    entry.getValue().run();
                }
                catch (Throwable ex) {
                    System.err.println("Exception thrown while executing destruction callback for session bean [" + entry.getKey() + "]: " + ex);
                }
            }
        }
        
        // Remove all beans in the session
        this.sessionBeanMap.remove(sessionId);
    }
    
    /**
     * Session ID resolver interface
     * Used to get current session ID
     */
    @FunctionalInterface
    public interface SessionIdResolver {
        /**
         * Resolve current session ID
         * @return current session ID
         */
        String resolveSessionId();
    }
} 