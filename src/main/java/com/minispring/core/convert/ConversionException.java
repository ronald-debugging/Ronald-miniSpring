package com.minispring.core.convert;

/**
 * Type conversion exception
 * Thrown when type conversion fails
 */
public class ConversionException extends RuntimeException {
    
    /**
     * Create a new type conversion exception
     * @param message exception message
     */
    public ConversionException(String message) {
        super(message);
    }
    
    /**
     * Create a new type conversion exception
     * @param message exception message
     * @param cause original exception
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
} 