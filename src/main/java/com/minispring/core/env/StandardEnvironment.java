package com.minispring.core.env;

/**
 * Standard environment implementation
 * Provides standard system properties and environment variables
 */
public class StandardEnvironment extends AbstractEnvironment {
    
    /**
     * System properties property source name
     */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
    
    /**
     * System environment variables property source name
     */
    public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
    
    /**
     * Customize property sources
     * Add system properties and environment variables
     * 
     * @param propertySources property sources collection
     */
    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.addLast(new MapPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, getSystemEnvironment()));
    }
} 