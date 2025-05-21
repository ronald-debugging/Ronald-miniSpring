package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.DisposableBean;

import java.lang.reflect.Method;

/**
 * DisposableBean Adapter
 * Used to uniformly handle beans that implement DisposableBean interface and beans configured with destroy-method
 */
public class DisposableBeanAdapter implements DisposableBean {
    
    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;
    
    /**
     * Constructor
     * 
     * @param bean target bean
     * @param beanName bean name
     * @param destroyMethodName destroy method name
     */
    public DisposableBeanAdapter(Object bean, String beanName, String destroyMethodName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = destroyMethodName;
    }
    
    /**
     * Execute bean's destruction method
     * 1. If bean implements DisposableBean interface, call its destroy method
     * 2. If bean is configured with destroy-method, call that method via reflection
     */
    @Override
    public void destroy() throws Exception {
        // 1. If bean implements DisposableBean interface, call its destroy method
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
            System.out.println("Executing destroy method of DisposableBean interface for bean [" + beanName + "]");
        }
        
        // 2. If bean has configured destroy-method and it's not the DisposableBean interface method, call it via reflection
        if (destroyMethodName != null && !(bean instanceof DisposableBean && "destroy".equals(destroyMethodName))) {
            try {
                Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
                destroyMethod.invoke(bean);
                System.out.println("Executing custom destroy method for bean [" + beanName + "]: " + destroyMethodName);
            } catch (NoSuchMethodException e) {
                throw new BeansException("Could not find destroy method [" + destroyMethodName + "] for bean [" + beanName + "]", e);
            } catch (Exception e) {
                throw new BeansException("Failed to execute destroy method [" + destroyMethodName + "] for bean [" + beanName + "]", e);
            }
        }
    }
} 