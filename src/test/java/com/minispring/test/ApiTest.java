package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.UserDao;
import com.minispring.test.bean.UserDaoImpl;
import com.minispring.test.bean.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * API test class
 * Used for testing basic IoC container functionality
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        // 1. Create Bean factory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. Register UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(UserDaoImpl.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);

        // 3. Register UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(UserService.class);
        // Set initialization method
        userServiceBeanDefinition.setInitMethodName("init");
        
        // Set properties
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "Zhang San"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", beanFactory.getBean("userDao")));
        userServiceBeanDefinition.setPropertyValues(propertyValues);
        
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);

        // 4. Get Bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        
        // 5. Use Bean
        String result = userService.queryUserInfo();
        System.out.println("Test result: " + result);
        
        // 6. Verify result
        Assertions.assertEquals("Beijing", result);
    }
    
    @Test
    public void testPropertyValues() {
        // Test PropertyValues functionality
        PropertyValues propertyValues = new PropertyValues();
        
        // Test adding properties
        propertyValues.addPropertyValue(new PropertyValue("name", "Zhang San"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        
        // Test getting properties
        Assertions.assertTrue(propertyValues.contains("name"));
        Assertions.assertTrue(propertyValues.contains("age"));
        Assertions.assertFalse(propertyValues.contains("address"));
        
        // Test property count
        Assertions.assertEquals(2, propertyValues.size());
        
        // Test getting property values
        Assertions.assertEquals("Zhang San", propertyValues.getPropertyValue("name").orElseThrow().getValue());
        Assertions.assertEquals(18, propertyValues.getPropertyValue("age").orElseThrow().getValue());
        
        // Test replacing property
        propertyValues.addPropertyValue(new PropertyValue("name", "Li Si"));
        Assertions.assertEquals("Li Si", propertyValues.getPropertyValue("name").orElseThrow().getValue());
        Assertions.assertEquals(2, propertyValues.size());
        
        // Test converting value
        PropertyValue nameProperty = propertyValues.getPropertyValue("name").orElseThrow();
        nameProperty.setConvertedValue("Wang Wu");
        Assertions.assertEquals("Wang Wu", nameProperty.getConvertedValue());
        Assertions.assertEquals("Li Si", nameProperty.getValue());
    }
} 