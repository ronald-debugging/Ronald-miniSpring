package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * Converter for converting String to Character
 */
public class StringToCharacterConverter implements Converter<String, Character> {
    
    @Override
    public Character convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        if (source.length() > 1) {
            throw new ConversionException("String [" + source + "] has more than one character");
        }
        return source.charAt(0);
    }
} 