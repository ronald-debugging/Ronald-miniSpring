package com.minispring.test;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.DefaultResourceLoader;
import com.minispring.core.io.Resource;
import com.minispring.test.bean.LifecycleBean;
import com.minispring.test.bean.PrototypeBean;
import com.minispring.test.bean.TestBean;
import com.minispring.test.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean definition reader test class
 */
public class XmlBeanDefinitionReaderTest {
    
    private DefaultListableBeanFactory beanFactory;
    private XmlBeanDefinitionReader beanDefinitionReader;
    
    @BeforeEach
    void setUp() {
        // Create Bean factory
        beanFactory = new DefaultListableBeanFactory();
        // Create Bean definition reader
        beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    }
    
    @Test
    void testLoadBeanDefinitions() {
        // Load Bean definitions
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:spring.xml");
        beanDefinitionReader.loadBeanDefinitions(resource);
        
        // Verify Bean definition count
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
        
        // Verify Bean definition content
        BeanDefinition testBeanDefinition = beanFactory.getBeanDefinition("testBean");
        assertEquals(TestBean.class, testBeanDefinition.getBeanClass());
        assertEquals(1, testBeanDefinition.getPropertyValues().getPropertyValues().length);
        
        BeanDefinition testServiceDefinition = beanFactory.getBeanDefinition("testService");
        assertEquals(TestService.class, testServiceDefinition.getBeanClass());
        assertEquals(2, testServiceDefinition.getPropertyValues().getPropertyValues().length);
        
        BeanDefinition lifecycleBeanDefinition = beanFactory.getBeanDefinition("lifecycleBean");
        assertEquals(LifecycleBean.class, lifecycleBeanDefinition.getBeanClass());
        assertEquals("init", lifecycleBeanDefinition.getInitMethodName());
        assertEquals("destroy", lifecycleBeanDefinition.getDestroyMethodName());
        
        BeanDefinition prototypeBeanDefinition = beanFactory.getBeanDefinition("prototypeBean");
        assertEquals(PrototypeBean.class, prototypeBeanDefinition.getBeanClass());
        assertTrue(prototypeBeanDefinition.isPrototype());
        assertFalse(prototypeBeanDefinition.isSingleton());
    }
    
    @Test
    void testLoadBeanDefinitionsFromLocation() {
        // Load Bean definitions from location
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        
        // Verify Bean definition count
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
    }
    
    @Test
    void testLoadBeanDefinitionsFromMultipleLocations() {
        // Load Bean definitions from multiple locations
        beanDefinitionReader.loadBeanDefinitions(
                "classpath:spring.xml"
        );
        
        // Verify Bean definition count
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
    }
} 