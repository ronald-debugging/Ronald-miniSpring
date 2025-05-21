package com.minispring.core.env;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Mutable property sources collection
 * Provides add, remove, and modify operations for property sources collection
 */
public class MutablePropertySources implements PropertySources {
    
    /**
     * Property sources list, using thread-safe CopyOnWriteArrayList
     */
    private final List<PropertySource<?>> propertySources;
    
    /**
     * Default constructor
     */
    public MutablePropertySources() {
        this.propertySources = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Constructor with existing property sources list
     * 
     * @param propertySources existing property sources list
     */
    public MutablePropertySources(List<PropertySource<?>> propertySources) {
        this.propertySources = new CopyOnWriteArrayList<>(propertySources);
    }
    
    /**
     * Get property sources list, returns copy to avoid modification
     * 
     * @return copy of property sources list
     */
    public List<PropertySource<?>> getPropertySources() {
        return new ArrayList<>(this.propertySources);
    }
    
    /**
     * Add property source to top
     * 
     * @param propertySource property source to add
     */
    public void addFirst(PropertySource<?> propertySource) {
        removeIfPresent(propertySource);
        this.propertySources.add(0, propertySource);
    }
    
    /**
     * Add property source to bottom
     * 
     * @param propertySource property source to add
     */
    public void addLast(PropertySource<?> propertySource) {
        removeIfPresent(propertySource);
        this.propertySources.add(propertySource);
    }
    
    /**
     * Add property source before specified name
     * 
     * @param relativePropertySourceName relative property source name
     * @param propertySource property source to add
     * @throws IllegalArgumentException if relative property source doesn't exist
     */
    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = indexOf(relativePropertySourceName);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Property source '" + relativePropertySourceName + "' does not exist");
        }
        this.propertySources.add(index, propertySource);
    }
    
    /**
     * Add property source after specified name
     * 
     * @param relativePropertySourceName relative property source name
     * @param propertySource property source to add
     * @throws IllegalArgumentException if relative property source doesn't exist
     */
    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = indexOf(relativePropertySourceName);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Property source '" + relativePropertySourceName + "' does not exist");
        }
        this.propertySources.add(index + 1, propertySource);
    }
    
    /**
     * Replace property source with specified name
     * 
     * @param name property source name to replace
     * @param propertySource new property source
     * @throws IllegalArgumentException if property source with specified name doesn't exist
     */
    public void replace(String name, PropertySource<?> propertySource) {
        int index = indexOf(name);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Property source '" + name + "' does not exist");
        }
        this.propertySources.set(index, propertySource);
    }
    
    /**
     * Remove property source with specified name
     * 
     * @param name property source name to remove
     * @return removed property source, returns null if not exists
     */
    public PropertySource<?> remove(String name) {
        int index = indexOf(name);
        if (index != -1) {
            return this.propertySources.remove(index);
        }
        return null;
    }
    
    /**
     * Get property source with specified name
     * 
     * @param name property source name
     * @return property source, returns null if not exists
     */
    @Override
    public PropertySource<?> get(String name) {
        for (PropertySource<?> propertySource : this.propertySources) {
            if (propertySource.getName().equals(name)) {
                return propertySource;
            }
        }
        return null;
    }
    
    /**
     * Check if contains property source with specified name
     * 
     * @param name property source name
     * @return returns true if contains, false otherwise
     */
    @Override
    public boolean contains(String name) {
        return this.propertySources.stream()
                .anyMatch(propertySource -> propertySource.getName().equals(name));
    }
    
    /**
     * Return iterator of property sources
     * 
     * @return property sources iterator
     */
    @Override
    public Iterator<PropertySource<?>> iterator() {
        return this.propertySources.iterator();
    }
    
    /**
     * Remove property source if exists
     * 
     * @param propertySource property source to remove
     */
    private void removeIfPresent(PropertySource<?> propertySource) {
        this.propertySources.removeIf(source -> source.getName().equals(propertySource.getName()));
    }
    
    /**
     * Check if relative addition is legal
     * 
     * @param relativePropertySourceName relative property source name
     * @param propertySource property source to add
     * @throws IllegalArgumentException if relative addition is illegal
     */
    private void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (relativePropertySourceName == null) {
            throw new IllegalArgumentException("Relative property source name cannot be null");
        }
        if (propertySource == null) {
            throw new IllegalArgumentException("Property source cannot be null");
        }
        if (relativePropertySourceName.equals(propertySource.getName())) {
            throw new IllegalArgumentException("Relative property source and property source to add cannot have same name");
        }
    }
    
    /**
     * Get index of property source with specified name
     * 
     * @param name property source name
     * @return index, returns -1 if not exists
     */
    private int indexOf(String name) {
        for (int i = 0; i < this.propertySources.size(); i++) {
            if (this.propertySources.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Override toString method
     */
    @Override
    public String toString() {
        return this.propertySources.toString();
    }
} 