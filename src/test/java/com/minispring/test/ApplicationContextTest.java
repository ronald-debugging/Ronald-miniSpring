package com.minispring.test;

import com.minispring.context.ApplicationContext;
import com.minispring.context.support.ClassPathXmlApplicationContext;
import com.minispring.test.bean.TestBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApplicationContext test class
 * Used for testing basic ApplicationContext functionality
 */
public class ApplicationContextTest {
    
    /**
     * Test loading Bean from classpath XML
     */
    @Test
    public void testClassPathXmlApplicationContext() {
        // Create ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        
        // Get Bean
        TestBean testBean = applicationContext.getBean("testBean", TestBean.class);
        
        // Verify Bean
        assertNotNull(testBean, "Bean should not be null");
        assertEquals("Test Bean", testBean.getName(), "Property value should be correct");
        
        // Test singleton
        TestBean testBean2 = applicationContext.getBean("testBean", TestBean.class);
        assertSame(testBean, testBean2, "Should be the same Bean instance");
        
        // Test BeanFactory methods
        assertTrue(applicationContext.containsBean("testBean"), "Should contain testBean");
        assertFalse(applicationContext.containsBean("nonExistBean"), "Should not contain nonExistBean");
        
        // Test Bean list
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        assertNotNull(beanNames, "Bean names array should not be null");
        assertTrue(beanNames.length > 0, "Bean names array should contain elements");
        
        // Output all Bean names
        System.out.println("All Bean names:");
        for (String name : beanNames) {
            System.out.println("- " + name);
        }
    }
}