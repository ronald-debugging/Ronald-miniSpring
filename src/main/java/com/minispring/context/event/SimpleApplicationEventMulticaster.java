package com.minispring.context.event;

import com.minispring.beans.factory.BeanFactory;
import com.minispring.context.ApplicationEvent;
import com.minispring.context.ApplicationListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Application Event Multicaster Implementation
 * Maintains a list of listeners and notifies all matching listeners when events occur
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {
    
    /**
     * List of listeners
     */
    private final List<ApplicationListener<?>> listeners = new ArrayList<>();
    
    /**
     * Bean factory, used to obtain listeners
     */
    private BeanFactory beanFactory;
    
    /**
     * Default constructor
     */
    public SimpleApplicationEventMulticaster() {
    }
    
    /**
     * Constructor
     * 
     * @param beanFactory Bean factory
     */
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    /**
     * Set Bean factory
     * 
     * @param beanFactory Bean factory
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    /**
     * Add event listener
     * 
     * @param listener listener to add
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove event listener
     * 
     * @param listener listener to remove
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        listeners.remove(listener);
    }
    
    /**
     * Remove all listeners
     */
    @Override
    public void removeAllListeners() {
        listeners.clear();
    }
    
    /**
     * Multicast event to all matching listeners
     * 
     * @param event event to multicast
     */
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : getApplicationListeners(event)) {
            invokeListener(listener, event);
        }
    }
    
    /**
     * Get all listeners matching the event
     * 
     * @param event event
     * @return list of matching listeners
     */
    private Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        List<ApplicationListener> allListeners = new ArrayList<>();
        for (ApplicationListener<?> listener : listeners) {
            if (supportsEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }
    
    /**
     * Check if listener supports the given event
     * 
     * @param listener listener
     * @param event event
     * @return true if supported
     */
    private boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event) {
        // Simplified implementation, all listeners support all events by default
        // In actual Spring, reflection is used to check generic types
        return true;
    }
    
    /**
     * Invoke listener to handle event
     * 
     * @param listener listener
     * @param event event
     */
    @SuppressWarnings("unchecked")
    private void invokeListener(ApplicationListener listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        } catch (Exception e) {
            // Log or rethrow exception
            System.err.println("Error occurred while handling event: " + e.getMessage());
        }
    }
} 