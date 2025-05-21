package com.minispring.core.env;

import java.util.Properties;

/**
 * System properties property source implementation
 * Gets property values from system properties
 */
public class SystemPropertySource extends PropertySource<Properties> {
    
    /**
     * Default system properties property source name
     */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
    
    /**
     * Constructor, uses default name and system properties
     */
    public SystemPropertySource() {
        super(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, System.getProperties());
    }
    
    /**
     * Constructor, uses specified name and system properties
     * 
     * @param name property source name
     */
    public SystemPropertySource(String name) {
        super(name, System.getProperties());
    }
    
    /**
     * Get property value by name
     * 
     * @param name property name
     * @return property value, returns null if not exists
     */
    @Override
    public Object getProperty(String name) {
        return this.source.getProperty(name);
    }
} 