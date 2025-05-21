package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Singleton Bean Registry Implementation
 * Implements SingletonBeanRegistry interface, providing registration and retrieval of singleton beans
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** First-level cache: fully initialized singleton object cache */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    
    /** Second-level cache: early exposed singleton objects (not fully initialized) cache */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    
    /** Third-level cache: singleton factory cache, used to store bean creation factories for later proxy creation */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    
    /** Set of singleton bean names currently in creation */
    private final Set<String> singletonsCurrentlyInCreation = ConcurrentHashMap.newKeySet();
    
    /**
     * Container for beans that need to be destroyed
     * Using LinkedHashMap to ensure destruction order is reverse of registration order
     */
    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

    /**
     * Get singleton bean
     * Implements three-level cache lookup
     * 
     * @param beanName bean name
     * @return singleton bean object, returns null if not found
     */
    @Override
    public Object getSingleton(String beanName) {
        // First try to get from first-level cache
        Object singletonObject = singletonObjects.get(beanName);
        
        // If not in first-level cache and bean is currently in creation (possible circular dependency)
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                // Try to get from second-level cache
                singletonObject = earlySingletonObjects.get(beanName);
                
                // If not in second-level cache, try third-level cache
                if (singletonObject == null) {
                    ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        // Get object through factory
                        singletonObject = singletonFactory.getObject();
                        // Put in second-level cache and remove from third-level cache
                        earlySingletonObjects.put(beanName, singletonObject);
                        singletonFactories.remove(beanName);
                    }
                }
            }
        }
        
        return singletonObject;
    }
    
    /**
     * Get singleton bean
     * If not exists, create through provided ObjectFactory
     * 
     * @param beanName bean name
     * @param singletonFactory bean factory
     * @return singleton bean
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            // First check first-level cache
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                
                // Mark bean as currently in creation
                beforeSingletonCreation(beanName);
                
                boolean newSingleton = false;
                try {
                    // Create singleton using factory
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                } catch (Exception ex) {
                    throw new BeansException("Failed to create singleton bean [" + beanName + "]", ex);
                } finally {
                    // Clear in-creation mark
                    afterSingletonCreation(beanName);
                }
                
                if (newSingleton) {
                    // Add created singleton to first-level cache and remove from second and third level caches
                    addSingleton(beanName, singletonObject);
                }
            }
            
            return singletonObject;
        }
    }
    
    /**
     * Add singleton bean to first-level cache and clear second and third level caches
     * 
     * @param beanName bean name
     * @param singletonObject singleton bean
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }
    
    /**
     * Add singleton factory to third-level cache
     * 
     * @param beanName bean name
     * @param singletonFactory singleton factory
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
            }
        }
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }
    
    /**
     * Check if bean is currently in creation
     * 
     * @param beanName bean name
     * @return whether bean is currently in creation
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }
    
    /**
     * Mark specified bean as currently in creation
     * 
     * @param beanName bean name
     */
    protected void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeansException("Bean [" + beanName + "] is already in creation");
        }
    }
    
    /**
     * Mark specified bean creation as complete
     * 
     * @param beanName bean name
     */
    protected void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new BeansException("Bean [" + beanName + "] is not in creation, cannot end creation process");
        }
    }

    /**
     * Register bean that needs to be destroyed
     * 
     * @param beanName bean name
     * @param bean bean that needs to be destroyed
     */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }
    
    /**
     * Destroy singleton beans
     * Destroy beans in reverse order of registration
     */
    public void destroySingletons() {
        Set<String> beanNames = disposableBeans.keySet();
        String[] disposableBeanNames = beanNames.toArray(new String[0]);
        
        // Destroy beans in reverse order of registration
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            String beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Exception occurred while destroying bean [" + beanName + "]", e);
            }
        }
        
        // Clear all caches
        this.singletonObjects.clear();
        this.earlySingletonObjects.clear();
        this.singletonFactories.clear();
        this.singletonsCurrentlyInCreation.clear();
    }

    /**
     * Check if contains singleton bean with specified name
     * @param beanName bean name
     * @return whether contains
     */
    protected boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    /**
     * Get names of all singleton beans
     * @return array of singleton bean names
     */
    protected String[] getSingletonNames() {
        return singletonObjects.keySet().toArray(new String[0]);
    }

    /**
     * Get count of singleton beans
     * @return count of singleton beans
     */
    protected int getSingletonCount() {
        return singletonObjects.size();
    }
}