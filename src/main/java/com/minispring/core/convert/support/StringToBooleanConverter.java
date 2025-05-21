package com.minispring.core.convert.support;

import com.minispring.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * Converter for converting String to Boolean
 * Supports the following true values:
 * "true", "on", "yes", "y", "1"
 * 
 * Supports the following false values:
 * "false", "off", "no", "n", "0"
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {
    
    private static final Set<String> TRUE_VALUES = new HashSet<>(8);
    private static final Set<String> FALSE_VALUES = new HashSet<>(8);
    
    static {
        // Initialize true value set
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("y");
        TRUE_VALUES.add("1");
        
        // Initialize false value set
        FALSE_VALUES.add("false");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("n");
        FALSE_VALUES.add("0");
    }
    
    @Override
    public Boolean convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        
        String value = source.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) {
            return Boolean.TRUE;
        }
        else if (FALSE_VALUES.contains(value)) {
            return Boolean.FALSE;
        }
        else {
            // Default return false
            return Boolean.FALSE;
        }
    }
} 