package com.minispring.core.convert;

/**
 * Type conversion service interface
 * Provides unified type conversion entry point
 */
public interface ConversionService {
    
    /**
     * Determine if source type object can be converted to target type
     * @param sourceType source type
     * @param targetType target type
     * @return returns true if conversion is possible
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);
    
    /**
     * Convert source object to target type
     * @param source source object
     * @param targetType target type
     * @param <T> target type generic
     * @return converted target type object
     * @throws ConversionException if conversion fails
     */
    <T> T convert(Object source, Class<T> targetType);
} 