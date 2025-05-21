package com.minispring.beans;

/**
 * Type Converter Interface
 * Used to convert values to target types during property injection
 */
public interface TypeConverter {

    /**
     * Convert value to specified type
     * @param value value to convert
     * @param requiredType target type
     * @param <T> target type
     * @return converted value
     * @throws TypeMismatchException if conversion fails
     */
    <T> T convert(Object value, Class<T> requiredType) throws TypeMismatchException;
}