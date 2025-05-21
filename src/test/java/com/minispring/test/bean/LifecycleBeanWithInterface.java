package com.minispring.test.bean;

import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.InitializingBean;

/**
 * Lifecycle Bean class (using interface approach)
 * Implements InitializingBean and DisposableBean interfaces
 */
public class LifecycleBeanWithInterface implements InitializingBean, DisposableBean {
    
    private String name;
    private boolean initialized = false;
    private boolean destroyed = false;
    
    public LifecycleBeanWithInterface() {
        System.out.println("LifecycleBeanWithInterface constructor");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("LifecycleBeanWithInterface afterPropertiesSet method");
        this.initialized = true;
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("LifecycleBeanWithInterface destroy method");
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
        return "LifecycleBeanWithInterface{" +
                "name='" + name + '\'' +
                ", initialized=" + initialized +
                ", destroyed=" + destroyed +
                '}';
    }
} 