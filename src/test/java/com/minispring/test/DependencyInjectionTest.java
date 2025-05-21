package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.TestUserController;
import com.minispring.test.bean.TestUserDao;
import com.minispring.test.bean.TestUserService;
import com.minispring.test.bean.TestUserServiceWithConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dependency injection test class
 */
public class DependencyInjectionTest {

    /**
     * Test property injection
     */
    @Test
    public void testPropertyInjection() {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // Create UserService property values
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "Zhang San"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        
        // Register UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);
        
        // Get UserService
        TestUserService userService = (TestUserService) beanFactory.getBean("userService");
        
        // Verify property injection
        assertEquals("Zhang San", userService.getName());
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * Test constructor injection
     */
    @Test
    public void testConstructorInjection() {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // Register UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserServiceWithConstructor.class);
        beanFactory.registerBeanDefinition("userServiceWithConstructor", userServiceBeanDefinition);
        
        // Get UserService, inject UserDao through constructor
        System.out.println("Starting to get userServiceWithConstructor");
        TestUserServiceWithConstructor userService = (TestUserServiceWithConstructor) beanFactory.getBean("userServiceWithConstructor", new Object[]{beanFactory.getBean("userDao")});
        System.out.println("Got userServiceWithConstructor: " + userService);
        System.out.println("userDao: " + userService.getUserDao());
        
        // Verify constructor injection
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * Test autowiring
     */
    @Test
    public void testAutowiring() {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // Register UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserServiceWithConstructor.class);
        beanFactory.registerBeanDefinition("userServiceWithConstructor", userServiceBeanDefinition);
        
        // Get UserService, inject UserDao through autowiring
        System.out.println("Starting to get userServiceWithConstructor (autowiring)");
        TestUserServiceWithConstructor userService = (TestUserServiceWithConstructor) beanFactory.getBean("userServiceWithConstructor");
        System.out.println("Got userServiceWithConstructor: " + userService);
        System.out.println("userDao: " + userService.getUserDao());
        
        // Verify autowiring
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * Test nested dependency
     */
    @Test
    public void testNestedDependency() {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // Register UserService
        PropertyValues userServicePropertyValues = new PropertyValues();
        userServicePropertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserService.class, userServicePropertyValues);
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);
        
        // Register UserController
        PropertyValues userControllerPropertyValues = new PropertyValues();
        userControllerPropertyValues.addPropertyValue(new PropertyValue("userService", new BeanReference("userService")));
        BeanDefinition userControllerBeanDefinition = new BeanDefinition(TestUserController.class, userControllerPropertyValues);
        beanFactory.registerBeanDefinition("userController", userControllerBeanDefinition);
        
        // Get UserController
        TestUserController userController = (TestUserController) beanFactory.getBean("userController");
        
        // Verify nested dependency
        assertNotNull(userController.getUserService());
        assertNotNull(userController.getUserService().getUserDao());
        assertEquals("UserDao", userController.getUserService().getUserDao().toString());
    }
} 