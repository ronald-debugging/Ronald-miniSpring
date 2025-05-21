package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converter for converting String to Date
 * Supports multiple date formats
 */
public class StringToDateConverter implements Converter<String, Date> {
    
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd",
            "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm",
            "yyyy.MM.dd"
    };
    
    @Override
    public Date convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        
        String value = source.trim();
        
        // Try different date formats
        for (String format : DATE_FORMATS) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                return dateFormat.parse(value);
            }
            catch (ParseException ex) {
                // Try next format
            }
        }
        
        throw new ConversionException("Cannot parse date from String [" + source + "]");
    }
} 