package com.minispring.core.convert.converter;

/**
 * Conditional Converter Interface
 * Can determine whether conversion can be performed based on specific conditions
 */
public interface ConditionalConverter {
    
    /**
     * Determine if source type can be converted to target type
     * @param sourceType source type
     * @param targetType target type
     * @return true if conversion is possible
     */
    boolean matches(Class<?> sourceType, Class<?> targetType);
} 