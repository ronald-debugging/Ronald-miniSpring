package com.minispring.core.env;

import java.util.Iterator;

/**
 * Property sources collection interface
 * Manages a group of PropertySource
 */
public interface PropertySources extends Iterable<PropertySource<?>> {
    
    /**
     * Return property source with specified name
     * 
     * @param name property source name
     * @return property source, returns null if not exists
     */
    PropertySource<?> get(String name);
    
    /**
     * Check if contains property source with specified name
     * 
     * @param name property source name
     * @return returns true if contains, false otherwise
     */
    boolean contains(String name);
    
    /**
     * Return iterator of property sources
     * 
     * @return property sources iterator
     */
    @Override
    Iterator<PropertySource<?>> iterator();
} 