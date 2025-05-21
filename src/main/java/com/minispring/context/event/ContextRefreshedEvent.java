package com.minispring.context.event;

import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationEvent;

/**
 * Context Refreshed Event
 * Published when ApplicationContext is initialized or refreshed
 */
public class ContextRefreshedEvent extends ApplicationEvent {
    
    /**
     * Constructor
     * 
     * @param source event source (application context)
     */
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
    
    /**
     * Get application context
     * 
     * @return application context
     */
    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
} 