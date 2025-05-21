package com.minispring.context.event;

import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationEvent;

/**
 * Context Closed Event
 * Published when ApplicationContext is closed
 */
public class ContextClosedEvent extends ApplicationEvent {
    
    /**
     * Constructor
     * 
     * @param source event source (application context)
     */
    public ContextClosedEvent(ApplicationContext source) {
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