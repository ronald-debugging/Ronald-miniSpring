package com.minispring.beans;

/**
 * Bean Property Value
 * Stores Bean property name, original value and converted value
 */
public class PropertyValue {

    private final String name;
    private final Object value;
    private Object convertedValue;

    /**
     * Create a new PropertyValue instance
     * @param name property name
     * @param value property original value
     */
    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get property name
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Get property original value
     * @return property original value
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Get converted property value
     * @return converted property value, returns null if not converted
     */
    public Object getConvertedValue() {
        return convertedValue;
    }
    
    /**
     * Set converted property value
     * @param convertedValue converted property value
     */
    public void setConvertedValue(Object convertedValue) {
        this.convertedValue = convertedValue;
    }
} 