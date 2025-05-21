package com.minispring.beans.factory.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Dependency Descriptor
 * Used to describe dependency injection points for fields or method parameters
 * Provides foundation for supporting annotations like @Autowired
 */
public class DependencyDescriptor {

    private Field field;
    private Parameter methodParameter;
    private Method method;
    private int parameterIndex;
    private Class<?> declaringClass;
    private boolean required;
    private String dependencyName;
    private String parameterName;

    /**
     * Create a field dependency descriptor
     * 
     * @param field field to be injected
     * @param required whether injection is required
     */
    public DependencyDescriptor(Field field, boolean required) {
        this.field = field;
        this.declaringClass = field.getDeclaringClass();
        this.required = required;
    }

    /**
     * Create a method parameter dependency descriptor
     * 
     * @param method method
     * @param parameterIndex parameter index
     * @param required whether injection is required
     */
    public DependencyDescriptor(Method method, int parameterIndex, boolean required) {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.methodParameter = method.getParameters()[parameterIndex];
        this.declaringClass = method.getDeclaringClass();
        this.required = required;
    }

    /**
     * Create a constructor parameter dependency descriptor
     * 
     * @param parameter constructor parameter
     * @param required whether injection is required
     */
    public DependencyDescriptor(Parameter parameter, boolean required) {
        this.methodParameter = parameter;
        this.declaringClass = parameter.getDeclaringExecutable().getDeclaringClass();
        this.required = required;
    }

    /**
     * Get dependency type
     * 
     * @return dependency type
     */
    public Class<?> getDependencyType() {
        if (this.field != null) {
            return this.field.getType();
        }
        if (this.methodParameter != null) {
            return this.methodParameter.getType();
        }
        return null;
    }

    /**
     * Get dependency name
     * 
     * @return dependency name
     */
    public String getDependencyName() {
        if (this.parameterName != null) {
            return this.parameterName;
        }
        if (this.field != null) {
            return this.field.getName();
        }
        if (this.methodParameter != null) {
            return this.methodParameter.getName();
        }
        return null;
    }

    /**
     * Set dependency name
     * 
     * @param dependencyName dependency name
     */
    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    /**
     * Get field
     * 
     * @return field
     */
    public Field getField() {
        return this.field;
    }

    /**
     * Get method parameter
     * 
     * @return method parameter
     */
    public Parameter getMethodParameter() {
        return this.methodParameter;
    }

    /**
     * Get method
     * 
     * @return method
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Get parameter index
     * 
     * @return parameter index
     */
    public int getParameterIndex() {
        return this.parameterIndex;
    }

    /**
     * Get declaring class
     * 
     * @return declaring class
     */
    public Class<?> getDeclaringClass() {
        return this.declaringClass;
    }

    /**
     * Check if injection is required
     * 
     * @return whether injection is required
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Set parameter name
     * 
     * @param parameterName parameter name
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
} 