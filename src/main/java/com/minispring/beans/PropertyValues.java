package com.minispring.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Bean Property Values Collection
 * Used to store and manage multiple PropertyValue objects
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * Add property value
     * If property with same name exists, it will be replaced
     * @param propertyValue property value object, cannot be null
     * @throws IllegalArgumentException if propertyValue is null
     */
    public void addPropertyValue(PropertyValue propertyValue) {
        if (propertyValue == null) {
            throw new IllegalArgumentException("PropertyValue cannot be null");
        }
        
        // Remove existing property with same name
        this.propertyValueList.removeIf(existing ->
                existing.getName().equals(propertyValue.getName()));
        this.propertyValueList.add(propertyValue);
    }

    /**
     * Get all property values
     * @return array of property values, will not be null, may be empty array
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * Get property value by name
     * @param propertyName property name
     * @return Optional containing property value, empty if not found
     */
    public Optional<PropertyValue> getPropertyValue(String propertyName) {
        if (propertyName == null) {
            return Optional.empty();
        }
        
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(propertyName)) {
                return Optional.of(propertyValue);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Check if contains property with specified name
     * @param propertyName property name
     * @return true if contains
     */
    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName).isPresent();
    }
    
    /**
     * Get number of property values
     * @return number of property values
     */
    public int size() {
        return propertyValueList.size();
    }
    
    /**
     * Check if empty
     * @return true if empty
     */
    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }
    
    /**
     * Get unmodifiable property value list
     * @return unmodifiable property value list
     */
    public List<PropertyValue> getPropertyValueList() {
        return Collections.unmodifiableList(propertyValueList);
    }
} 