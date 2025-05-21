package com.minispring.core.env;

import java.util.Map;

/**
 * Environment variable property source implementation
 * Gets property values from system environment variables
 */
public class EnvironmentPropertySource extends MapPropertySource {
    
    /**
     * Default environment variable property source name
     */
    public static final String ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
    
    /**
     * Constructor, uses default name and system environment variables
     */
    public EnvironmentPropertySource() {
        super(ENVIRONMENT_PROPERTY_SOURCE_NAME, (Map) System.getenv());
    }
    
    /**
     * Constructor, uses specified name and system environment variables
     * 
     * @param name property source name
     */
    public EnvironmentPropertySource(String name) {
        super(name, (Map) System.getenv());
    }
    
    /**
     * Get property value by name
     * Special handling for environment variable names, supports different formats
     * 
     * @param name property name
     * @return property value, returns null if not exists
     */
    @Override
    public Object getProperty(String name) {
        // First try direct get
        Object value = super.getProperty(name);
        if (value != null) {
            return value;
        }
        
        // Try get after format conversion
        // For example, convert user.home to USER_HOME
        String alternateName = name.replace('.', '_').toUpperCase();
        return super.getProperty(alternateName);
    }
    
    /**
     * Check if property source contains property by name
     * 
     * @param name property name
     * @return returns true if contains, false otherwise
     */
    @Override
    public boolean containsProperty(String name) {
        // First try direct check
        if (super.containsProperty(name)) {
            return true;
        }
        
        // Try check after format conversion
        String alternateName = name.replace('.', '_').toUpperCase();
        return super.containsProperty(alternateName);
    }
} 