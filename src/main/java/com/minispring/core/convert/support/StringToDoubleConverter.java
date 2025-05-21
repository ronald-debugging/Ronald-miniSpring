package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * Converter for converting String to Double
 */
public class StringToDoubleConverter implements Converter<String, Double> {
    
    @Override
    public Double convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Double: " + ex.getMessage(), ex);
        }
    }
} 