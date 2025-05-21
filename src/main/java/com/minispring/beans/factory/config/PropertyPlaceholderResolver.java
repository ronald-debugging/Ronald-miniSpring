package com.minispring.beans.factory.config;

import java.util.Properties;

/**
 * Property Placeholder Resolver
 * Used to resolve placeholders in ${...} format
 */
public class PropertyPlaceholderResolver implements PlaceholderResolver {
    
    /**
     * Placeholder prefix
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    
    /**
     * Placeholder suffix
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    
    /**
     * Separator between placeholder and default value
     */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";
    
    private final String placeholderPrefix;
    private final String placeholderSuffix;
    private final String valueSeparator;
    private final Properties properties;
    
    /**
     * Create resolver with default prefix, suffix and separator
     * 
     * @param properties property source
     */
    public PropertyPlaceholderResolver(Properties properties) {
        this(DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX, DEFAULT_VALUE_SEPARATOR, properties);
    }
    
    /**
     * Create resolver with custom prefix, suffix and separator
     * 
     * @param placeholderPrefix placeholder prefix
     * @param placeholderSuffix placeholder suffix
     * @param valueSeparator separator between placeholder and default value
     * @param properties property source
     */
    public PropertyPlaceholderResolver(String placeholderPrefix, String placeholderSuffix, 
                                      String valueSeparator, Properties properties) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        this.valueSeparator = valueSeparator;
        this.properties = properties;
    }
    
    @Override
    public String resolvePlaceholders(String value) {
        if (value == null || value.isEmpty() || !containsPlaceholder(value)) {
            return value;
        }
        
        StringBuilder result = new StringBuilder(value);
        int startIndex = result.indexOf(placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = result.indexOf(placeholderSuffix, startIndex + placeholderPrefix.length());
            if (endIndex != -1) {
                // Extract placeholder content
                String placeholder = result.substring(startIndex + placeholderPrefix.length(), endIndex);
                String defaultValue = null;
                
                // Check if there's a default value
                int separatorIndex = placeholder.indexOf(valueSeparator);
                if (separatorIndex != -1) {
                    defaultValue = placeholder.substring(separatorIndex + valueSeparator.length());
                    placeholder = placeholder.substring(0, separatorIndex);
                }
                
                // Look up property value
                String propVal = properties.getProperty(placeholder);
                if (propVal == null && defaultValue != null) {
                    propVal = defaultValue;
                }
                
                if (propVal != null) {
                    // Replace placeholder
                    result.replace(startIndex, endIndex + placeholderSuffix.length(), propVal);
                    // Update next search position
                    startIndex = result.indexOf(placeholderPrefix, startIndex + propVal.length());
                } else {
                    // No property value found, keep original placeholder
                    startIndex = result.indexOf(placeholderPrefix, endIndex + placeholderSuffix.length());
                }
            } else {
                // No end marker found, exit loop
                break;
            }
        }
        
        return result.toString();
    }
    
    @Override
    public boolean containsPlaceholder(String value) {
        return value != null && value.contains(placeholderPrefix) && value.contains(placeholderSuffix);
    }
} 