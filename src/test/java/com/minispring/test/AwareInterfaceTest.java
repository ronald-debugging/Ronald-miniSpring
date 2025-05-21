package com.minispring.test;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.AwareBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Aware interface family test class
 */
public class AwareInterfaceTest {
    
    @Test
    void testBeanNameAwareAndBeanFactoryAware() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register Bean definition
        BeanDefinition beanDefinition = new BeanDefinition(AwareBean.class);
        beanFactory.registerBeanDefinition("awareBean", beanDefinition);
        
        // Get Bean
        AwareBean awareBean = beanFactory.getBean("awareBean", AwareBean.class);
        
        // Verify BeanNameAware interface
        assertEquals("awareBean", awareBean.getBeanName());
        
        // Verify BeanFactoryAware interface
        assertSame(beanFactory, awareBean.getBeanFactory());
        
        System.out.println("BeanNameAware interface test passed: " + awareBean.getBeanName());
        System.out.println("BeanFactoryAware interface test passed: " + awareBean.getBeanFactory());
    }
} 