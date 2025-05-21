package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.config.BeanFactoryPostProcessor;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationEvent;
import com.minispring.context.ApplicationListener;
import com.minispring.context.ConfigurableApplicationContext;
import com.minispring.context.event.ApplicationEventMulticaster;
import com.minispring.context.event.ContextClosedEvent;
import com.minispring.context.event.ContextRefreshedEvent;
import com.minispring.context.event.SimpleApplicationEventMulticaster;
import com.minispring.core.env.ConfigurableEnvironment;
import com.minispring.core.env.Environment;
import com.minispring.core.env.StandardEnvironment;
import com.minispring.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract implementation of ApplicationContext interface
 * Provides template method pattern for context refresh process
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    
    /**
     * Application context name
     */
    private String applicationName = "";
    
    /**
     * Application context startup time
     */
    private long startupDate;
    
    /**
     * Event multicaster
     */
    private ApplicationEventMulticaster applicationEventMulticaster;
    
    /**
     * Environment object
     */
    private ConfigurableEnvironment environment;
    
    /**
     * Default constructor
     */
    public AbstractApplicationContext() {
    }
    
    /**
     * Refresh application context
     * This is a template method that defines the standard process for refreshing context
     * 
     * @throws BeansException if an error occurs during refresh
     */
    @Override
    public void refresh() throws BeansException {
        // 1. Prepare refresh context environment
        prepareRefresh();
    
        // 2. Create BeanFactory and load BeanDefinition
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        
        // 3. Prepare BeanFactory, set class loader, etc.
        prepareBeanFactory(beanFactory);
        
        try {
            // 4. Allow subclasses to modify application context's internal BeanFactory after standard initialization
            postProcessBeanFactory(beanFactory);
            
            // 5. Call BeanFactoryPostProcessor to modify BeanDefinition
            invokeBeanFactoryPostProcessors(beanFactory);
            
            // 6. Register BeanPostProcessor, these processors are used during bean initialization
            registerBeanPostProcessors(beanFactory);
            
            // 7. Initialize event multicaster
            initApplicationEventMulticaster();
            
            // 8. Initialize other beans specific to subclasses
            onRefresh();
            
            // 9. Register listeners
            registerListeners();
            
            // 10. Complete instantiation of all singleton beans
            finishBeanFactoryInitialization(beanFactory);
            
            // 11. Complete refresh process, publish event
            finishRefresh();
        } catch (BeansException ex) {
            // 12. Destroy created singleton beans
            destroyBeans();
            
            // 13. Reset context active flag
            cancelRefresh(ex);
            
            // Rethrow exception
            throw ex;
        }
    }
    
    /**
     * Prepare refresh context environment
     */
    protected void prepareRefresh() {
        // Record startup time
        this.startupDate = System.currentTimeMillis();
        
        // Initialize environment variables
        initPropertySources();
        
        // Get environment and validate required properties
        getEnvironment();
    }
    
    /**
     * Initialize property sources
     * Subclasses can override this method to customize initialization logic
     */
    protected void initPropertySources() {
        // Default implementation is empty, subclasses can override
    }
    
    /**
     * Get new BeanFactory
     * 
     * @return ConfigurableListableBeanFactory
     */
    protected abstract ConfigurableListableBeanFactory obtainFreshBeanFactory();
    
    /**
     * Prepare BeanFactory
     * 
     * @param beanFactory BeanFactory
     */
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // Add ApplicationContextAwareProcessor to handle Aware interfaces
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }
    
    /**
     * Allow subclasses to modify application context's internal BeanFactory after standard initialization
     * 
     * @param beanFactory BeanFactory
     * @throws BeansException if an error occurs during processing
     */
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Default implementation is empty, subclasses can override
    }
    
    /**
     * Call BeanFactoryPostProcessor to modify BeanDefinition
     * 
     * @param beanFactory BeanFactory
     * @throws BeansException if an error occurs during processing
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Get all beans of type BeanFactoryPostProcessor
        Map<String, BeanFactoryPostProcessor> postProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor postProcessor : postProcessorMap.values()) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }
    
    /**
     * Register BeanPostProcessor, these processors are used during bean initialization
     * 
     * @param beanFactory BeanFactory
     * @throws BeansException if an error occurs during processing
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Get all beans of type BeanPostProcessor
        Map<String, BeanPostProcessor> postProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor postProcessor : postProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }
    
    /**
     * Initialize event multicaster
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
    }
    
    /**
     * Initialize other beans specific to subclasses
     * 
     * @throws BeansException if an error occurs during initialization
     */
    protected void onRefresh() throws BeansException {
        // Default implementation is empty, subclasses can override
    }
    
    /**
     * Register listeners
     */
    protected void registerListeners() {
        // Get all beans of type ApplicationListener
        Collection<ApplicationListener> listeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : listeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }
    
    /**
     * Complete instantiation of all singleton beans
     * 
     * @param beanFactory BeanFactory
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        // Initialize all remaining singleton beans
        beanFactory.preInstantiateSingletons();
    }
    
    /**
     * Complete refresh process, publish event
     */
    protected void finishRefresh() {
        // Record startup time
        this.startupDate = System.currentTimeMillis();
        
        // Publish context refreshed event
        publishEvent(new ContextRefreshedEvent(this));
    }
    
    /**
     * Destroy created singleton beans
     */
    protected void destroyBeans() {
        getBeanFactory().destroySingletons();
    }
    
    /**
     * Reset context active flag
     * 
     * @param ex exception that caused cancellation
     */
    protected void cancelRefresh(BeansException ex) {
        // Default implementation is empty
    }
    
    /**
     * Publish application event
     * 
     * @param event event to publish
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }
    
    /**
     * Close application context
     */
    @Override
    public void close() {
        // Publish context closed event
        publishEvent(new ContextClosedEvent(this));
        
        // Destroy all singleton beans
        destroyBeans();
    }
    
    /**
     * Get Bean factory
     * 
     * @return ConfigurableListableBeanFactory
     * @throws IllegalStateException if factory has not been created
     */
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
    
    /**
     * Set application context name
     * 
     * @param applicationName application context name
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    
    /**
     * Get application context name
     * 
     * @return application context name
     */
    @Override
    public String getApplicationName() {
        return this.applicationName;
    }
    
    /**
     * Get application context startup time
     * 
     * @return startup time (milliseconds)
     */
    @Override
    public long getStartupDate() {
        return this.startupDate;
    }
    
    /**
     * Get bean from Bean factory
     * 
     * @param name bean name
     * @return bean instance
     * @throws BeansException if getting bean fails
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }
    
    /**
     * Get bean of specified type from Bean factory
     * 
     * @param name bean name
     * @param requiredType bean type
     * @return bean instance
     * @throws BeansException if getting bean fails
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }
    
    /**
     * Get bean of specified type from Bean factory
     * 
     * @param requiredType bean type
     * @return bean instance
     * @throws BeansException if getting bean fails
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }
    
    /**
     * Get bean with constructor arguments from Bean factory
     * 
     * @param name bean name
     * @param args constructor arguments
     * @return bean instance
     * @throws BeansException if getting bean fails
     */
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }
    
    /**
     * Check if bean exists
     * 
     * @param name bean name
     * @return true if exists
     */
    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }
    
    /**
     * Get all beans of specified type
     * 
     * @param type bean type
     * @return map of bean names to bean instances
     * @throws BeansException if getting fails
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }
    
    /**
     * Get all bean definition names
     * 
     * @return array of bean definition names
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
    
    /**
     * Get current environment object
     * If not exists, create standard environment
     * 
     * @return environment object
     */
    @Override
    public ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = createEnvironment();
        }
        return this.environment;
    }
    
    /**
     * Create environment object
     * Subclasses can override this method to provide custom environment
     * 
     * @return newly created environment object
     */
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }
    
    /**
     * Set environment object
     * 
     * @param environment environment object
     */
    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }
} 