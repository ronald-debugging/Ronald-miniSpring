package com.minispring.core.env;

/**
 * Environment interface
 * Represents the current application running environment
 * Provides property access and Profile functionality
 */
public interface Environment {
    
    /**
     * Get property value by name
     * 
     * @param key property name
     * @return property value, returns null if not exists
     */
    String getProperty(String key);
    
    /**
     * Get property value by name, use default value if not exists
     * 
     * @param key property name
     * @param defaultValue default value
     * @return property value, returns default value if not exists
     */
    String getProperty(String key, String defaultValue);
    
    /**
     * Get property value by name and convert to specified type
     * 
     * @param key property name
     * @param targetType target type
     * @param <T> target type generic
     * @return property value, returns null if not exists
     */
    <T> T getProperty(String key, Class<T> targetType);
    
    /**
     * Get property value by name and convert to specified type, use default value if not exists
     * 
     * @param key property name
     * @param targetType target type
     * @param defaultValue default value
     * @param <T> target type generic
     * @return property value, returns default value if not exists
     */
    <T> T getProperty(String key, Class<T> targetType, T defaultValue);
    
    /**
     * Check if property exists by name
     * 
     * @param key property name
     * @return returns true if exists, false otherwise
     */
    boolean containsProperty(String key);
    
    /**
     * Get currently active profiles
     * 
     * @return active profiles array
     */
    String[] getActiveProfiles();
    
    /**
     * Get default profiles
     * 
     * @return default profiles array
     */
    String[] getDefaultProfiles();
    
    /**
     * Check if specified profile is active
     * 
     * @param profile profile name
     * @return returns true if active, false otherwise
     */
    boolean acceptsProfiles(String profile);
    
    /**
     * Check if any of specified profiles is active
     * 
     * @param profiles profile names array
     * @return returns true if any is active, false otherwise
     */
    boolean acceptsProfiles(String... profiles);
} 