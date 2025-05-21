package com.minispring.core.convert.converter;

/**
 * Converter Registry Interface
 * Used for registering and managing type converters
 */
public interface ConverterRegistry {
    
    /**
     * Register a converter
     * @param converter converter instance
     */
    void addConverter(Converter<?, ?> converter);
    
    /**
     * Register a converter for specific types
     * @param sourceType source type
     * @param targetType target type
     * @param converter converter
     * @param <S> source type generic
     * @param <T> target type generic
     */
    <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter);
    
    /**
     * Remove all converters
     */
    void removeConvertibles();
} 