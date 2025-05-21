package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionService;
import com.minispring.core.convert.converter.Converter;
import com.minispring.core.convert.converter.ConverterFactory;
import com.minispring.core.convert.converter.ConverterRegistry;
import com.minispring.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * Default type conversion service implementation
 * Implements both ConversionService and ConverterRegistry interfaces
 */
public class DefaultConversionService implements ConversionService, ConverterRegistry {
    
    private static final DefaultConversionService SHARED_INSTANCE = new DefaultConversionService();
    
    private final GenericConversionService conversionService;
    
    /**
     * Create a new DefaultConversionService instance
     */
    public DefaultConversionService() {
        this.conversionService = new GenericConversionService();
        registerDefaultConverters();
    }
    
    /**
     * Get shared DefaultConversionService instance
     * @return shared instance
     */
    public static ConversionService getSharedInstance() {
        return SHARED_INSTANCE;
    }
    
    /**
     * Register default type converters
     */
    public void registerDefaultConverters() {
        // Register basic type converters
        addConverter(new StringToIntegerConverter());
        addConverter(new StringToLongConverter());
        addConverter(new StringToDoubleConverter());
        addConverter(new StringToFloatConverter());
        addConverter(new StringToBooleanConverter());
        addConverter(new StringToCharacterConverter());
        addConverter(new StringToShortConverter());
        addConverter(new StringToByteConverter());
        
        // Register date-time converters
        addConverter(new StringToDateConverter());
        
        // Register collection converters
        // More collection type converters can be extended here
    }
    
    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return this.conversionService.canConvert(sourceType, targetType);
    }
    
    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        return this.conversionService.convert(source, targetType);
    }
    
    @Override
    public void addConverter(Converter<?, ?> converter) {
        this.conversionService.addConverter(converter);
    }
    
    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        this.conversionService.addConverter(sourceType, targetType, converter);
    }
    
    @Override
    public void removeConvertibles() {
        this.conversionService.removeConvertibles();
    }
    
    /**
     * Add converter factory
     * @param factory converter factory
     */
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        this.conversionService.addConverterFactory(factory);
    }
    
    /**
     * Add generic converter
     * @param converter generic converter
     */
    public void addConverter(GenericConverter converter) {
        this.conversionService.addConverter(converter);
    }
} 