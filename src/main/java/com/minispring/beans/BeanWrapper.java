package com.minispring.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean Wrapper
 * Used to encapsulate Bean instances
 * Provides access to Bean properties, supports nested properties
 */
public class BeanWrapper {

    private final Object wrappedInstance;
    private Class<?> wrappedClass;
    private TypeConverter typeConverter;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
        this.typeConverter = new SimpleTypeConverter();
    }

    /**
     * Get wrapped Bean instance
     * @return Bean instance
     */
    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    /**
     * Get wrapped Bean instance type
     * @return Bean instance type
     */
    public Class<?> getWrappedClass() {
        return this.wrappedClass;
    }
    
    /**
     * Get property value, supports nested properties (e.g. "person.address.city")
     * 
     * @param propertyName property name
     * @return property value
     * @throws BeansException if getting property value fails
     */
    public Object getPropertyValue(String propertyName) throws BeansException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new BeansException("Property name cannot be empty");
        }
        
        // Handle nested properties
        if (propertyName.contains(".")) {
            String[] propertyPath = propertyName.split("\\.", 2);
            String currentProperty = propertyPath[0];
            String remainingPath = propertyPath[1];
            
            Object nestedValue = getPropertyValueInternal(wrappedInstance, currentProperty);
            if (nestedValue == null) {
                return null;
            }
            
            BeanWrapper nestedWrapper = new BeanWrapper(nestedValue);
            return nestedWrapper.getPropertyValue(remainingPath);
        }
        
        // Handle simple properties
        return getPropertyValueInternal(wrappedInstance, propertyName);
    }
    
    /**
     * Set property value, supports nested properties (e.g. "person.address.city")
     * 
     * @param propertyName property name
     * @param value property value
     * @throws BeansException if setting property value fails
     */
    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new BeansException("Property name cannot be empty");
        }
        
        // Handle nested properties
        if (propertyName.contains(".")) {
            String[] propertyPath = propertyName.split("\\.", 2);
            String currentProperty = propertyPath[0];
            String remainingPath = propertyPath[1];
            
            Object nestedValue = getPropertyValueInternal(wrappedInstance, currentProperty);
            if (nestedValue == null) {
                // If nested object is null, try to create a new instance
                try {
                    Class<?> propertyType = getPropertyType(wrappedClass, currentProperty);
                    if (propertyType == null) {
                        throw new BeansException("Cannot determine property type: " + currentProperty);
                    }
                    
                    nestedValue = propertyType.getDeclaredConstructor().newInstance();
                    setPropertyValueInternal(wrappedInstance, currentProperty, nestedValue);
                } catch (Exception e) {
                    throw new BeansException("Cannot create nested object: " + currentProperty, e);
                }
            }
            
            BeanWrapper nestedWrapper = new BeanWrapper(nestedValue);
            nestedWrapper.setPropertyValue(remainingPath, value);
            return;
        }
        
        // Handle simple properties
        setPropertyValueInternal(wrappedInstance, propertyName, value);
    }
    
    /**
     * Internal method: Get property value
     * 
     * @param object target object
     * @param propertyName property name
     * @return property value
     * @throws BeansException if getting property value fails
     */
    private Object getPropertyValueInternal(Object object, String propertyName) throws BeansException {
        try {
            // First try to get through getter method
            String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Method getterMethod = object.getClass().getMethod(getterMethodName);
                return getterMethod.invoke(object);
            } catch (NoSuchMethodException e) {
                // If no getter method, try boolean type isXxx method
                if (propertyName.startsWith("is")) {
                    getterMethodName = propertyName;
                } else {
                    getterMethodName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                }
                
                try {
                    Method isMethod = object.getClass().getMethod(getterMethodName);
                    return isMethod.invoke(object);
                } catch (NoSuchMethodException e1) {
                    // If no getter and isXxx methods, try direct field access
                    Field field = object.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    return field.get(object);
                }
            }
        } catch (Exception e) {
            throw new BeansException("Failed to get property value: " + propertyName, e);
        }
    }
    
    /**
     * Internal method: Set property value
     * 
     * @param object target object
     * @param propertyName property name
     * @param value property value
     * @throws BeansException if setting property value fails
     */
    private void setPropertyValueInternal(Object object, String propertyName, Object value) throws BeansException {
        try {
            // Get property type
            Class<?> propertyType = getPropertyType(object.getClass(), propertyName);
            if (propertyType == null) {
                throw new BeansException("Cannot determine property type: " + propertyName);
            }
            
            // Type conversion
            Object convertedValue = value;
            if (value != null && !propertyType.isInstance(value)) {
                convertedValue = typeConverter.convert(value, propertyType);
            }
            
            // First try to set through setter method
            String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Method setterMethod = object.getClass().getMethod(setterMethodName, propertyType);
                setterMethod.invoke(object, convertedValue);
                return;
            } catch (NoSuchMethodException e) {
                // If no setter method, try direct field setting
                Field field = object.getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(object, convertedValue);
            }
        } catch (TypeMismatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BeansException("Failed to set property value: " + propertyName, e);
        }
    }
    
    /**
     * Get property type
     * 
     * @param beanClass Bean type
     * @param propertyName property name
     * @return property type, returns null if not found
     */
    private Class<?> getPropertyType(Class<?> beanClass, String propertyName) {
        // First try to get type through getter method
        String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        try {
            Method getterMethod = beanClass.getMethod(getterMethodName);
            return getterMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            // If no getter method, try boolean type isXxx method
            if (propertyName.startsWith("is")) {
                getterMethodName = propertyName;
            } else {
                getterMethodName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            }
            
            try {
                Method isMethod = beanClass.getMethod(getterMethodName);
                return isMethod.getReturnType();
            } catch (NoSuchMethodException e1) {
                // If no getter and isXxx methods, try to get type through setter method
                String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                        return method.getParameterTypes()[0];
                    }
                }
                
                // If no setter method, try to get field type directly
                try {
                    Field field = beanClass.getDeclaredField(propertyName);
                    return field.getType();
                } catch (NoSuchFieldException e2) {
                    return null;
                }
            }
        }
    }
} 