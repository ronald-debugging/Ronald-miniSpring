package com.minispring.core.convert.converter;

/**
 * Converter Factory Interface
 * Creates corresponding converters based on target type
 * 
 * @param <S> source type
 * @param <R> base class of target type
 */
public interface ConverterFactory<S, R> {
    
    /**
     * Get converter from S to target type T, where T is a subclass of R
     * 
     * @param targetType target type
     * @param <T> generic parameter of target type, subclass of R
     * @return corresponding converter
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
} 