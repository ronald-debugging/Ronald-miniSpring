package com.minispring.core.convert.converter;

/**
 * Type Converter Interface
 * Converts objects of type S to type T
 * @param <S> source type
 * @param <T> target type
 */
@FunctionalInterface
public interface Converter<S, T> {
    
    /**
     * Convert source object to target type
     * @param source source object, will not be null
     * @return converted object, may be null
     */
    T convert(S source);
} 