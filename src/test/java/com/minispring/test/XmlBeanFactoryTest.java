package com.minispring.test;

import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.test.bean.LifecycleBean;
import com.minispring.test.bean.PrototypeBean;
import com.minispring.test.bean.TestBean;
import com.minispring.test.service.TestService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean factory test class
 * Test loading Bean definitions from XML and instantiating Beans
 */
public class XmlBeanFactoryTest {
    
    @Test
    void testXmlBeanFactory() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create Bean definition reader
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        
        // Load Bean definitions
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        
        // Get and verify TestBean
        TestBean testBean = beanFactory.getBean("testBean", TestBean.class);
        assertNotNull(testBean);
        assertEquals("Test Bean", testBean.getName());
        
        // Get and verify TestService
        TestService testService = beanFactory.getBean("testService", TestService.class);
        assertNotNull(testService);
        assertEquals("Hello, MiniSpring!", testService.getMessage());
        assertSame(testBean, testService.getTestBean());
        
        // Get and verify LifecycleBean
        LifecycleBean lifecycleBean = beanFactory.getBean("lifecycleBean", LifecycleBean.class);
        assertNotNull(lifecycleBean);
        assertEquals("Lifecycle", lifecycleBean.getName());
        assertTrue(lifecycleBean.isInitialized());
        assertFalse(lifecycleBean.isDestroyed());
        
        // Test prototype Bean
        PrototypeBean prototypeBean1 = beanFactory.getBean("prototypeBean", PrototypeBean.class);
        PrototypeBean prototypeBean2 = beanFactory.getBean("prototypeBean", PrototypeBean.class);
        assertNotNull(prototypeBean1);
        assertNotNull(prototypeBean2);
        assertEquals("Prototype", prototypeBean1.getName());
        assertEquals("Prototype", prototypeBean2.getName());
        assertNotSame(prototypeBean1, prototypeBean2);
    }
} 