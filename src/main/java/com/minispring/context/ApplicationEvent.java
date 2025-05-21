package com.minispring.context;

import java.util.EventObject;

/**
 * Application Event Base Class
 * All application events should extend this class
 */
public abstract class ApplicationEvent extends EventObject {
    
    /**
     * Event occurrence time
     */
    private final long timestamp;
    
    /**
     * Constructor
     * 
     * @param source event source object
     */
    public ApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Get event occurrence time
     * 
     * @return timestamp (milliseconds)
     */
    public long getTimestamp() {
        return timestamp;
    }
} 