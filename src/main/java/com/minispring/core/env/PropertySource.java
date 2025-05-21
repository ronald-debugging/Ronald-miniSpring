package com.minispring.core.env;

/**
 * Abstract property source class
 * Represents a key-value pair form property source
 * 
 * @param <T> underlying source type of property source
 */
public abstract class PropertySource<T> {
    
    /**
     * Property source name
     */
    private final String name;
    
    /**
     * Underlying source of property source
     */
    protected final T source;
    
    /**
     * Constructor
     * 
     * @param name property source name
     * @param source underlying source of property source
     */
    public PropertySource(String name, T source) {
        this.name = name;
        this.source = source;
    }
    
    /**
     * Get property source name
     * 
     * @return property source name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get underlying source of property source
     * 
     * @return underlying source of property source
     */
    public T getSource() {
        return this.source;
    }
    
    /**
     * Check if property source contains property by name
     * 
     * @param name property name
     * @return returns true if contains, false otherwise
     */
    public boolean containsProperty(String name) {
        return getProperty(name) != null;
    }
    
    /**
     * Get property value by name
     * 
     * @param name property name
     * @return property value, returns null if not exists
     */
    public abstract Object getProperty(String name);
    
    /**
     * Override equals method
     * Determine equality by property source name
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PropertySource)) {
            return false;
        }
        PropertySource<?> otherSource = (PropertySource<?>) other;
        return this.name.equals(otherSource.name);
    }
    
    /**
     * Override hashCode method
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    /**
     * Override toString method
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " {name='" + this.name + "'}";
    }
} 