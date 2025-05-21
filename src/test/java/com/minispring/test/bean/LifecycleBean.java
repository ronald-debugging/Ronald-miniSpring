package com.minispring.test.bean;

/**
 * Lifecycle Bean class
 * Used for testing initialization and destruction methods
 */
public class LifecycleBean {
    
    private String name;
    private boolean initialized = false;
    private boolean destroyed = false;
    
    public LifecycleBean() {
        System.out.println("LifecycleBean constructor");
    }
    
    public void init() {
        System.out.println("LifecycleBean initialization method");
        this.initialized = true;
    }
    
    public void destroy() {
        System.out.println("LifecycleBean destruction method");
        this.destroyed = true;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    @Override
    public String toString() {
        return "LifecycleBean{" +
                "name='" + name + '\'' +
                ", initialized=" + initialized +
                ", destroyed=" + destroyed +
                '}';
    }
} 