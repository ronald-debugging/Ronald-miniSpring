package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * Converter for converting String to Short
 */
public class StringToShortConverter implements Converter<String, Short> {
    
    @Override
    public Short convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Short.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Short: " + ex.getMessage(), ex);
        }
    }
} 