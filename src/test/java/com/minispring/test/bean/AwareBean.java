package com.minispring.test.bean;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.BeanFactoryAware;
import com.minispring.beans.factory.BeanNameAware;

/**
 * Aware interface test Bean class
 * Implements BeanNameAware and BeanFactoryAware interfaces
 */
public class AwareBean implements BeanNameAware, BeanFactoryAware {
    
    private String beanName;
    private BeanFactory beanFactory;
    
    @Override
    public void setBeanName(String name) {
        System.out.println("AwareBean.setBeanName: " + name);
        this.beanName = name;
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("AwareBean.setBeanFactory: " + beanFactory);
        this.beanFactory = beanFactory;
    }
    
    public String getBeanName() {
        return beanName;
    }
    
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
} 