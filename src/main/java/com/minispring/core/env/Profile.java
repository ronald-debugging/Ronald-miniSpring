package com.minispring.core.env;

/**
 * Profile interface
 * Represents one or more configuration environments
 */
public interface Profile {
    
    /**
     * Check if Profile is active
     * 
     * @return returns true if active, false otherwise
     */
    boolean isActive();
    
    /**
     * Get Profile name
     * 
     * @return Profile name
     */
    String getName();
} 