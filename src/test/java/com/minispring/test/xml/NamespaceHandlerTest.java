package com.minispring.test.xml;

import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.ClassPathResource;
import com.minispring.core.io.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Namespace handler test class
 */
public class NamespaceHandlerTest {
    
    @Test
    void testBeanDefinitionLoading() {
        // Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create XML Bean definition reader
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        
        // Load XML configuration file
        Resource resource = new ClassPathResource("bean-definitions.xml");
        reader.loadBeanDefinitions(resource);
        
        // Verify Bean definition is correctly loaded
        assertNotNull(beanFactory.getBeanDefinition("person"));
        assertNotNull(beanFactory.getBeanDefinition("address"));
        
        System.out.println("Successfully loaded Bean definitions: person, address");
    }
} 