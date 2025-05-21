package com.minispring.core.env;

import java.util.Map;

/**
 * Property source for handling system environment variables
 * System environment variables are typically uppercase and contain underscores
 * This class provides support for different formats of environment variables
 */
public class SystemEnvironmentPropertySource extends MapPropertySource {
    
    /**
     * Constructor
     * 
     * @param name property source name
     * @param source system environment variables map
     */
    public SystemEnvironmentPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
    
    /**
     * Get property value
     * Supports different formats of environment variable names
     * 
     * @param name property name
     * @return property value, returns null if not exists
     */
    @Override
    public Object getProperty(String name) {
        String actualKey = resolveKey(name);
        if (actualKey == null) {
            return null;
        }
        return super.getProperty(actualKey);
    }
    
    /**
     * Check if property exists
     * Supports different formats of environment variable names
     * 
     * @param name property name
     * @return returns true if exists, false otherwise
     */
    @Override
    public boolean containsProperty(String name) {
        String actualKey = resolveKey(name);
        return actualKey != null && super.containsProperty(actualKey);
    }
    
    /**
     * Resolve actual environment variable key name
     * Tries multiple formats: original, uppercase, uppercase with underscores, uppercase with dots, etc.
     * 
     * @param name property name
     * @return actual environment variable key name, returns null if not exists
     */
    private String resolveKey(String name) {
        // Try original key name
        if (super.containsProperty(name)) {
            return name;
        }
        
        // Try all uppercase
        String upperCase = name.toUpperCase();
        if (super.containsProperty(upperCase)) {
            return upperCase;
        }
        
        // Try replacing dots with underscores and uppercase
        String withUnderscores = upperCase.replace('.', '_');
        if (super.containsProperty(withUnderscores)) {
            return withUnderscores;
        }
        
        // Try replacing hyphens with underscores and uppercase
        String withUnderscoresForDashes = withUnderscores.replace('-', '_');
        if (super.containsProperty(withUnderscoresForDashes)) {
            return withUnderscoresForDashes;
        }
        
        return null;
    }
} 