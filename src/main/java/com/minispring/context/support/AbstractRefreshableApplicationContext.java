package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;

/**
 * Abstract implementation of refreshable ApplicationContext
 * Supports repeated refresh, creates new internal BeanFactory on each refresh
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    
    /**
     * Internal Bean factory
     */
    private DefaultListableBeanFactory beanFactory;
    
    /**
     * Get new BeanFactory
     * Creates new BeanFactory on each refresh
     * 
     * @return new BeanFactory
     */
    @Override
    protected final ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }
    
    /**
     * Refresh Bean factory
     * Create new Bean factory and load Bean definitions
     * 
     * @throws BeansException if an error occurs during creation or loading
     */
    protected void refreshBeanFactory() throws BeansException {
        // If BeanFactory exists, destroy all singleton beans and close factory
        if (this.beanFactory != null) {
            this.beanFactory.destroySingletons();
            this.beanFactory = null;
        }
        
        // Create new BeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        
        // Load Bean definitions
        loadBeanDefinitions(beanFactory);
        
        this.beanFactory = beanFactory;
    }
    
    /**
     * Create Bean factory
     * 
     * @return new DefaultListableBeanFactory
     */
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }
    
    /**
     * Load Bean definitions
     * Implemented by subclasses, can load Bean definitions from different sources
     * 
     * @param beanFactory Bean factory
     * @throws BeansException if an error occurs during loading
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;
    
    /**
     * Get Bean factory
     * 
     * @return Bean factory
     * @throws IllegalStateException if factory has not been created
     */
    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        if (this.beanFactory == null) {
            throw new IllegalStateException("BeanFactory has not been initialized or has been closed");
        }
        return this.beanFactory;
    }
}