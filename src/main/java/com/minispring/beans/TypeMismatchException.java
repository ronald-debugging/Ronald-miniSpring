package com.minispring.beans;

/**
 * Type Mismatch Exception
 * Thrown when type conversion fails
 */
public class TypeMismatchException extends BeansException {

    private final Class<?> requiredType;
    private final Object value;
    private String additionalContext;

    /**
     * Construct type mismatch exception
     * 
     * @param value value to convert
     * @param requiredType target type
     */
    public TypeMismatchException(Object value, Class<?> requiredType) {
        super("Cannot convert value '" + value + "' to type '" + requiredType.getName() + "'");
        this.value = value;
        this.requiredType = requiredType;
    }

    /**
     * Construct type mismatch exception
     * 
     * @param value value to convert
     * @param requiredType target type
     * @param cause original exception
     */
    public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause) {
        super("Cannot convert value '" + value + "' to type '" + requiredType.getName() + "'", cause);
        this.value = value;
        this.requiredType = requiredType;
    }

    /**
     * Construct type mismatch exception
     * @param message error message
     */
    public TypeMismatchException(String message) {
        super(message);
        this.value = null;
        this.requiredType = null;
    }

    /**
     * Get target type
     * 
     * @return target type
     */
    public Class<?> getRequiredType() {
        return this.requiredType;
    }

    /**
     * Get value to convert
     * 
     * @return value to convert
     */
    public Object getValue() {
        return this.value;
    }
    
    /**
     * Add context information
     * 
     * @param context context information
     * @return current exception instance for chaining
     */
    public TypeMismatchException addContext(String context) {
        this.additionalContext = context;
        return this;
    }
    
    /**
     * Get context information
     * 
     * @return context information
     */
    public String getAdditionalContext() {
        return this.additionalContext;
    }
    
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (additionalContext != null && !additionalContext.isEmpty()) {
            message = message + ". " + additionalContext;
        }
        return message;
    }
} 