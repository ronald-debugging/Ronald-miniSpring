package com.minispring.beans;

/**
 * Bean Exception
 * Base class for Spring Bean related exceptions
 */
public class BeansException extends RuntimeException {
    
    /**
     * Create a new Bean exception
     * @param message exception message
     */
    public BeansException(String message) {
        super(message);
    }
    
    /**
     * Create a new Bean exception
     * @param message exception message
     * @param cause original exception
     */
    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
} 