package com.minispring.beans.factory.config;

import com.minispring.beans.PropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean Definition
 * Used to define basic information of a Bean, such as class name, property values, initialization method, etc.
 */
public class BeanDefinition {

    private Class<?> beanClass;
    private PropertyValues propertyValues;
    private String initMethodName;
    private String destroyMethodName;
    private boolean singleton = true;
    private boolean prototype = false;
    
    // Scope
    private String scope = SCOPE_SINGLETON;
    
    // Whether scoped proxy is needed
    private boolean scopedProxy = false;
    
    // Custom attributes container
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Default scopes
     */
    public static String SCOPE_SINGLETON = "singleton";
    public static String SCOPE_PROTOTYPE = "prototype";

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    /**
     * Set the scope of the Bean
     * @param scope scope, possible values: singleton, prototype
     */
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }
    
    /**
     * Get the scope of the Bean
     * @return scope
     */
    public String getScope() {
        return this.scope;
    }
    
    /**
     * Set whether scoped proxy is needed
     * @param scopedProxy whether scoped proxy is needed
     */
    public void setScopedProxy(boolean scopedProxy) {
        this.scopedProxy = scopedProxy;
    }
    
    /**
     * Check if scoped proxy is needed
     * @return true if scoped proxy is needed
     */
    public boolean isScopedProxy() {
        return this.scopedProxy;
    }
    
    /**
     * Set custom attribute
     * @param name attribute name
     * @param value attribute value
     */
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }
    
    /**
     * Get custom attribute
     * @param name attribute name
     * @return attribute value
     */
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }
    
    /**
     * Check if specified custom attribute exists
     * @param name attribute name
     * @return true if attribute exists
     */
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }
} 