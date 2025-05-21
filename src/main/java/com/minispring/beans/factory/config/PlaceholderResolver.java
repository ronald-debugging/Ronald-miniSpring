package com.minispring.beans.factory.config;

/**
 * Placeholder Resolver Interface
 * Used to resolve placeholders in property values, such as ${...}
 */
public interface PlaceholderResolver {
    
    /**
     * Resolve a string containing placeholders
     * 
     * @param value string containing placeholders
     * @return resolved string
     */
    String resolvePlaceholders(String value);
    
    /**
     * Check if a string contains placeholders
     * 
     * @param value string to check
     * @return true if contains placeholders, false otherwise
     */
    boolean containsPlaceholder(String value);
} 