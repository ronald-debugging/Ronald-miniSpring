package com.minispring.core.env;

import java.util.Map;

/**
 * Configurable environment interface
 * Extends Environment, provides configuration functionality
 */
public interface ConfigurableEnvironment extends Environment {
    
    /**
     * Set active profiles
     * 
     * @param profiles active profiles array
     */
    void setActiveProfiles(String... profiles);
    
    /**
     * Add active profile
     * 
     * @param profile active profile
     */
    void addActiveProfile(String profile);
    
    /**
     * Set default profiles
     * 
     * @param profiles default profiles array
     */
    void setDefaultProfiles(String... profiles);
    
    /**
     * Get system properties
     * 
     * @return system properties
     */
    Map<String, Object> getSystemProperties();
    
    /**
     * Get system environment variables
     * 
     * @return system environment variables
     */
    Map<String, Object> getSystemEnvironment();
    
    /**
     * Merge configuration from another Environment
     * 
     * @param parent parent Environment
     */
    void merge(ConfigurableEnvironment parent);
    
    /**
     * Get property sources collection
     * 
     * @return property sources collection
     */
    MutablePropertySources getPropertySources();
} 