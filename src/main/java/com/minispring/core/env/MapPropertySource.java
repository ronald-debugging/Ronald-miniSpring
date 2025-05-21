package com.minispring.core.env;

import java.util.Map;

/**
 * Map-based property source implementation
 * Gets property values from Map
 */
public class MapPropertySource extends PropertySource<Map<String, Object>> {
    
    /**
     * Constructor
     * 
     * @param name property source name
     * @param source underlying Map of property source
     */
    public MapPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
    
    /**
     * Get property value by name
     * 
     * @param name property name
     * @return property value, returns null if not exists
     */
    @Override
    public Object getProperty(String name) {
        return this.source.get(name);
    }
    
    /**
     * Check if property source contains property by name
     * 
     * @param name property name
     * @return returns true if contains, false otherwise
     */
    @Override
    public boolean containsProperty(String name) {
        return this.source.containsKey(name);
    }
} 