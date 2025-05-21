package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.ConversionService;
import com.minispring.core.convert.converter.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generic type conversion service
 * Provides complete type conversion functionality and registration API
 */
public class GenericConversionService implements ConversionService, ConverterRegistry {
    
    /**
     * Type converter registry
     * Stores all registered generic converters
     */
    private final Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new ConcurrentHashMap<>(256);
    
    /**
     * Conversion cache for performance improvement
     */
    private final Map<Class<?>, Map<Class<?>, GenericConverter>> converterCache = new ConcurrentHashMap<>(256);
    
    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        if (sourceType == null) {
            return true;
        }
        if (targetType == null) {
            throw new IllegalArgumentException("Target type to convert to cannot be null");
        }
        
        // If source type is assignable to target type, no conversion needed
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }
        
        // Find converter, if found then conversion is possible
        return getConverter(sourceType, targetType) != null;
    }
    
    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        
        Class<?> sourceType = source.getClass();
        
        // If source type is assignable to target type, convert directly
        if (targetType.isAssignableFrom(sourceType)) {
            @SuppressWarnings("unchecked")
            T result = (T) source;
            return result;
        }
        
        // Find converter
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter == null) {
            throw new ConversionException("No converter found for converting from " + sourceType.getName() + " to " + targetType.getName());
        }
        
        // Execute conversion
        @SuppressWarnings("unchecked")
        T result = (T) converter.convert(source, sourceType, targetType);
        return result;
    }
    
    @Override
    public void addConverter(Converter<?, ?> converter) {
        ResolvableType[] typeInfo = getRequiredTypeInfo(converter.getClass(), Converter.class);
        Class<?> sourceType = typeInfo[0].resolve();
        Class<?> targetType = typeInfo[1].resolve();
        
        addConverter(new ConverterAdapter(converter, sourceType, targetType));
    }
    
    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        addConverter(new ConverterAdapter(converter, sourceType, targetType));
    }
    
    @Override
    public void removeConvertibles() {
        this.converters.clear();
        this.converterCache.clear();
    }
    
    /**
     * Add converter factory
     * @param factory converter factory
     */
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
        Class<?> sourceType = typeInfo[0].resolve();
        Class<?> targetType = typeInfo[1].resolve();
        
        addConverter(new ConverterFactoryAdapter(factory, sourceType, targetType));
    }
    
    /**
     * Add generic converter
     * @param converter generic converter
     */
    public void addConverter(GenericConverter converter) {
        Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        if (convertibleTypes != null) {
            for (GenericConverter.ConvertiblePair convertiblePair : convertibleTypes) {
                this.converters.put(convertiblePair, converter);
                this.converterCache.clear();
            }
        }
    }
    
    /**
     * Get converter from source type to target type
     * @param sourceType source type
     * @param targetType target type
     * @return found converter, returns null if not found
     */
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        // First check cache
        Map<Class<?>, GenericConverter> targetConverters = this.converterCache.get(sourceType);
        if (targetConverters != null) {
            GenericConverter converter = targetConverters.get(targetType);
            if (converter != null) {
                return converter;
            }
        }
        
        // Find all possible converters
        GenericConverter converter = find(sourceType, targetType);
        if (converter != null) {
            // Cache found converter
            if (targetConverters == null) {
                targetConverters = new ConcurrentHashMap<>(4);
                this.converterCache.put(sourceType, targetConverters);
            }
            targetConverters.put(targetType, converter);
        }
        
        return converter;
    }
    
    /**
     * Find all possible converters
     * @param sourceType source type
     * @param targetType target type
     * @return found converter, returns null if not found
     */
    private GenericConverter find(Class<?> sourceType, Class<?> targetType) {
        // Iterate all converters, find matching converter
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = this.converters.get(convertiblePair);
                if (converter != null) {
                    // Check conditional converter's matching conditions
                    if (!(converter instanceof ConditionalConverter) || 
                            ((ConditionalConverter) converter).matches(sourceType, targetType)) {
                        return converter;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get class hierarchy including all parent classes and interfaces
     * @param type type to process
     * @return class hierarchy list
     */
    private List<Class<?>> getClassHierarchy(Class<?> type) {
        List<Class<?>> hierarchy = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        collectClassHierarchy(type, hierarchy, visited);
        return hierarchy;
    }
    
    /**
     * Recursively collect class and all its parent classes and interfaces
     * @param type type to process
     * @param hierarchy class hierarchy list
     * @param visited set of visited types
     */
    private void collectClassHierarchy(Class<?> type, List<Class<?>> hierarchy, Set<Class<?>> visited) {
        if (type == null || visited.contains(type)) {
            return;
        }
        
        visited.add(type);
        hierarchy.add(type);
        
        // Collect interfaces
        for (Class<?> interfaceType : type.getInterfaces()) {
            collectClassHierarchy(interfaceType, hierarchy, visited);
        }
        
        // Collect parent class
        collectClassHierarchy(type.getSuperclass(), hierarchy, visited);
    }
    
    /**
     * Get required type information from converter type
     * @param converterClass converter class
     * @param genericIfc generic interface
     * @return type information array
     */
    private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
        ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
        ResolvableType[] generics = resolvableType.getGenerics();
        if (generics.length < 2) {
            throw new IllegalArgumentException("Unable to determine source and target types for converter " + 
                    converterClass.getName());
        }
        return generics;
    }
    
    /**
     * Converter adapter, adapts Converter to GenericConverter
     */
    private final class ConverterAdapter implements ConditionalGenericConverter {
        private final Converter<Object, Object> converter;
        private final GenericConverter.ConvertiblePair convertiblePair;
        private final ConversionService conversionService;
        
        @SuppressWarnings("unchecked")
        public ConverterAdapter(Converter<?, ?> converter, Class<?> sourceType, Class<?> targetType) {
            this.converter = (Converter<Object, Object>) converter;
            this.convertiblePair = new GenericConverter.ConvertiblePair(sourceType, targetType);
            this.conversionService = GenericConversionService.this;
        }
        
        @Override
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(this.convertiblePair);
        }
        
        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            // Simple implementation, always returns true, indicating unconditional match
            return true;
        }
        
        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            if (source == null) {
                return null;
            }
            return this.converter.convert(source);
        }
    }
    
    /**
     * ConverterFactory adapter, adapts ConverterFactory to GenericConverter
     */
    private final class ConverterFactoryAdapter implements ConditionalGenericConverter {
        private final ConverterFactory<Object, Object> converterFactory;
        private final Class<?> sourceType;
        private final Class<?> targetType;
        
        @SuppressWarnings("unchecked")
        public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, Class<?> sourceType, Class<?> targetType) {
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }
        
        @Override
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(this.sourceType, this.targetType));
        }
        
        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            // Check if targetType is a subclass of targetType
            return this.sourceType.isAssignableFrom(sourceType) && targetType.isAssignableFrom(this.targetType);
        }
        
        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            if (source == null) {
                return null;
            }
            return this.converterFactory.getConverter(targetType).convert(source);
        }
    }
    
    /**
     * Resolvable type, used for handling generic types
     */
    private static class ResolvableType {
        private final Class<?> resolved;
        private final ResolvableType superType;
        private final Class<?> rawClass;
        
        private ResolvableType(Class<?> resolved, ResolvableType superType, Class<?> rawClass) {
            this.resolved = resolved;
            this.superType = superType;
            this.rawClass = rawClass;
        }
        
        public static ResolvableType forClass(Class<?> clazz) {
            return new ResolvableType(clazz, null, clazz);
        }
        
        public ResolvableType as(Class<?> type) {
            if (this.rawClass != null && type.isAssignableFrom(this.rawClass)) {
                return forClass(type);
            }
            return forClass(type);
        }
        
        public ResolvableType[] getGenerics() {
            // Simplified implementation, returns empty array
            // Should actually resolve generic parameters
            return new ResolvableType[0];
        }
        
        public Class<?> resolve() {
            return this.resolved;
        }
    }
} 