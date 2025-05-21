package com.minispring.test;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.LifecycleBean;
import com.minispring.test.bean.LifecycleBeanWithInterface;
import com.minispring.test.processor.CustomBeanPostProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bean lifecycle test class
 * Test Bean initialization and destruction methods
 */
public class BeanLifecycleTest {
    
    @Test
    void testBeanLifecycle() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Add BeanPostProcessor
        beanFactory.addBeanPostProcessor(new CustomBeanPostProcessor());
        
        // Register Bean definition (using configuration method)
        BeanDefinition beanDefinition = new BeanDefinition(LifecycleBean.class);
        beanDefinition.setInitMethodName("init");
        beanDefinition.setDestroyMethodName("destroy");
        beanFactory.registerBeanDefinition("lifecycleBean", beanDefinition);
        
        // Register Bean definition (using interface method)
        BeanDefinition beanDefinition2 = new BeanDefinition(LifecycleBeanWithInterface.class);
        beanFactory.registerBeanDefinition("lifecycleBeanWithInterface", beanDefinition2);
        
        // Get Bean
        LifecycleBean lifecycleBean = beanFactory.getBean("lifecycleBean", LifecycleBean.class);
        LifecycleBeanWithInterface lifecycleBeanWithInterface = beanFactory.getBean("lifecycleBeanWithInterface", LifecycleBeanWithInterface.class);
        
        // Verify Bean is initialized
        assertTrue(lifecycleBean.isInitialized());
        assertFalse(lifecycleBean.isDestroyed());
        assertTrue(lifecycleBeanWithInterface.isInitialized());
        assertFalse(lifecycleBeanWithInterface.isDestroyed());
        
        // Destroy Bean
        beanFactory.destroySingletons();
        
        // Verify Bean is destroyed
        assertTrue(lifecycleBean.isDestroyed());
        assertTrue(lifecycleBeanWithInterface.isDestroyed());
    }
    
    @Test
    void testBeanPostProcessor() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Add BeanPostProcessor
        CustomBeanPostProcessor postProcessor = new CustomBeanPostProcessor();
        beanFactory.addBeanPostProcessor(postProcessor);
        
        // Register Bean definition
        BeanDefinition beanDefinition = new BeanDefinition(LifecycleBean.class);
        beanDefinition.setInitMethodName("init");
        beanFactory.registerBeanDefinition("lifecycleBean", beanDefinition);
        
        // Get Bean
        LifecycleBean lifecycleBean = beanFactory.getBean("lifecycleBean", LifecycleBean.class);
        
        // Verify Bean is initialized
        assertTrue(lifecycleBean.isInitialized());
    }
}