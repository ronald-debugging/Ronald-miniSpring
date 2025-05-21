package com.minispring.test.xml;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.ClassPathResource;
import com.minispring.core.io.Resource;
import com.minispring.test.bean.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean definition reader test class
 */
public class XmlBeanDefinitionReaderTest {
    
    @Test
    void testLoadBeanDefinitions() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create XML Bean definition reader
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        
        // Load XML configuration file
        Resource resource = new ClassPathResource("bean-definitions.xml");
        reader.loadBeanDefinitions(resource);
        
        // Verify Bean definition is correctly loaded
        assertTrue(beanFactory.containsBeanDefinition("person"));
        
        // Get Bean definition
        BeanDefinition personDefinition = beanFactory.getBeanDefinition("person");
        assertEquals(Person.class, personDefinition.getBeanClass());
        
        // Get Bean instance
        Person person = beanFactory.getBean("person", Person.class);
        assertNotNull(person);
        assertEquals("Zhang San", person.getName());
        assertEquals(30, person.getAge());
        
        // Verify reference injection
        assertTrue(beanFactory.containsBeanDefinition("address"));
        assertNotNull(person.getAddress());
        assertEquals("Beijing", person.getAddress().getCity());
        assertEquals("Chaoyang District", person.getAddress().getDistrict());
    }
} 