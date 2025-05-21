package com.minispring.test.bean;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.BeanFactoryAware;
import com.minispring.beans.factory.BeanNameAware;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.InitializingBean;
import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationContextAware;

/**
 * Test Bean class
 * Implements various Aware interfaces and lifecycle interfaces
 */
public class TestBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
    
    private String name;
    private String beanName;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    
    /**
     * Default constructor
     */
    public TestBean() {
        System.out.println("TestBean constructor executed");
    }
    
    /**
     * Constructor
     * 
     * @param name name
     */
    public TestBean(String name) {
        this.name = name;
        System.out.println("TestBean constructor executed, parameter name: " + name);
    }
    
    /**
     * Get name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set name
     * 
     * @param name name
     */
    public void setName(String name) {
        System.out.println("TestBean setting property name: " + name);
        this.name = name;
    }
    
    /**
     * Get BeanName
     * 
     * @return BeanName
     */
    public String getBeanName() {
        return beanName;
    }
    
    /**
     * Set BeanName (BeanNameAware interface)
     * 
     * @param name BeanName
     */
    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware interface called, beanName: " + name);
        this.beanName = name;
    }
    
    /**
     * Get BeanFactory
     * 
     * @return BeanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
    
    /**
     * Set BeanFactory (BeanFactoryAware interface)
     * 
     * @param beanFactory BeanFactory
     * @throws BeansException if error occurs during setting
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware interface called, setting beanFactory");
        this.beanFactory = beanFactory;
    }
    
    /**
     * Get ApplicationContext
     * 
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     * Set ApplicationContext (ApplicationContextAware interface)
     * 
     * @param applicationContext ApplicationContext
     * @throws BeansException if error occurs during setting
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware interface called, setting applicationContext");
        this.applicationContext = applicationContext;
    }
    
    /**
     * Initialization method (InitializingBean interface)
     * 
     * @throws Exception if error occurs during initialization
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean interface called, executing afterPropertiesSet method");
    }
    
    /**
     * Custom initialization method
     */
    public void init() {
        System.out.println("Custom initialization method init executed");
    }
    
    /**
     * Destruction method (DisposableBean interface)
     * 
     * @throws Exception if error occurs during destruction
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean interface called, executing destroy method");
    }
    
    /**
     * Custom destruction method
     */
    public void customDestroy() {
        System.out.println("Custom destruction method customDestroy executed");
    }
    
    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", beanName='" + beanName + '\'' +
                '}';
    }
} 