package com.minispring.context;

import java.util.EventListener;

/**
 * Application Event Listener Interface
 * Implements observer pattern, listens for application events
 * 
 * @param <E> event type
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    
    /**
     * Handle application event
     * 
     * @param event event to handle
     */
    void onApplicationEvent(E event);
} 