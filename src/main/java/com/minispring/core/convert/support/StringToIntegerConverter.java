package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * Converter for converting String to Integer
 */
public class StringToIntegerConverter implements Converter<String, Integer> {
    
    @Override
    public Integer convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Integer: " + ex.getMessage(), ex);
        }
    }
} 