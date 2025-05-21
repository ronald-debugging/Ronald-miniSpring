package com.minispring.context.event;

import com.minispring.context.ApplicationEvent;
import com.minispring.context.ApplicationListener;

/**
 * Application Event Multicaster Interface
 * Responsible for broadcasting events to all registered listeners
 */
public interface ApplicationEventMulticaster {
    
    /**
     * Add event listener
     * 
     * @param listener listener to add
     */
    void addApplicationListener(ApplicationListener<?> listener);
    
    /**
     * Remove event listener
     * 
     * @param listener listener to remove
     */
    void removeApplicationListener(ApplicationListener<?> listener);
    
    /**
     * Remove all listeners
     */
    void removeAllListeners();
    
    /**
     * Multicast event to all matching listeners
     * 
     * @param event event to multicast
     */
    void multicastEvent(ApplicationEvent event);
} 