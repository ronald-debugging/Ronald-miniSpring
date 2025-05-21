package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * Converter for converting String to Long
 */
public class StringToLongConverter implements Converter<String, Long> {
    
    @Override
    public Long convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Long: " + ex.getMessage(), ex);
        }
    }
} 