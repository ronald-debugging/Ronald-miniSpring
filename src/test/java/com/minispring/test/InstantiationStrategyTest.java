package com.minispring.test;

import com.minispring.beans.BeanWrapper;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.CglibSubclassingInstantiationStrategy;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.support.InstantiationStrategy;
import com.minispring.beans.factory.support.SimpleInstantiationStrategy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Instantiation strategy and Bean wrapper test
 */
public class InstantiationStrategyTest {

    /**
     * Test JDK reflection instantiation strategy
     */
    @Test
    public void testSimpleInstantiationStrategy() throws Exception {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        
        // Create JDK reflection instantiation strategy
        InstantiationStrategy strategy = new SimpleInstantiationStrategy();
        
        // Instantiate using no-args constructor
        Object bean1 = strategy.instantiate(beanDefinition, "testBean", null, null);
        assertNotNull(bean1);
        assertTrue(bean1 instanceof TestBean);
        
        // Instantiate using constructor with parameters
        Constructor<?> ctor = TestBean.class.getDeclaredConstructor(String.class, int.class);
        Object bean2 = strategy.instantiate(beanDefinition, "testBean", ctor, new Object[]{"test", 42});
        assertNotNull(bean2);
        assertTrue(bean2 instanceof TestBean);
        assertEquals("test", ((TestBean) bean2).getName());
        assertEquals(42, ((TestBean) bean2).getAge());
    }
    
    /**
     * Test CGLIB instantiation strategy
     */
    @Test
    public void testCglibSubclassingInstantiationStrategy() throws Exception {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        
        // Create CGLIB instantiation strategy
        InstantiationStrategy strategy = new CglibSubclassingInstantiationStrategy();
        
        // Instantiate using no-args constructor
        Object bean1 = strategy.instantiate(beanDefinition, "testBean", null, null);
        assertNotNull(bean1);
        assertTrue(bean1 instanceof TestBean);
        
        // Instantiate using constructor with parameters
        Constructor<?> ctor = TestBean.class.getDeclaredConstructor(String.class, int.class);
        Object bean2 = strategy.instantiate(beanDefinition, "testBean", ctor, new Object[]{"test", 42});
        assertNotNull(bean2);
        assertTrue(bean2 instanceof TestBean);
        assertEquals("test", ((TestBean) bean2).getName());
        assertEquals(42, ((TestBean) bean2).getAge());
    }
    
    /**
     * Test BeanWrapper
     */
    @Test
    public void testBeanWrapper() {
        // Create test Bean
        TestBean testBean = new TestBean("test", 42);
        
        // Create BeanWrapper
        BeanWrapper beanWrapper = new BeanWrapper(testBean);
        
        // Test getting wrapped Bean instance
        Object wrappedInstance = beanWrapper.getWrappedInstance();
        assertNotNull(wrappedInstance);
        assertTrue(wrappedInstance instanceof TestBean);
        assertEquals(testBean, wrappedInstance);
        
        // Test getting wrapped Bean type
        Class<?> wrappedClass = beanWrapper.getWrappedClass();
        assertEquals(TestBean.class, wrappedClass);
    }
    
    /**
     * Test type conversion and property population
     */
    @Test
    public void testPropertyValuesAndTypeConversion() {
        // Create BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        
        // Set property values
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "test"));
        propertyValues.addPropertyValue(new PropertyValue("age", "42")); // String type, needs to be converted to int
        beanDefinition.setPropertyValues(propertyValues);
        
        beanFactory.registerBeanDefinition("testBean", beanDefinition);
        
        // Get Bean
        TestBean testBean = (TestBean) beanFactory.getBean("testBean");
        
        // Verify property values
        assertEquals("test", testBean.getName());
        assertEquals(42, testBean.getAge());
    }
    
    /**
     * Test BeanWrapper's nested property support
     */
    @Test
    public void testNestedProperty() {
        // Create parent Bean
        TestBean parent = new TestBean("parent", 50);
        
        // Create child Bean
        TestBean child = new TestBean("child", 20);
        
        // Set parent-child relationship
        parent.setChild(child);
        
        // Create BeanWrapper
        BeanWrapper wrapper = new BeanWrapper(parent);
        
        // Test getting nested properties
        assertEquals("child", wrapper.getPropertyValue("child.name"));
        assertEquals(20, wrapper.getPropertyValue("child.age"));
        
        // Test setting nested properties
        wrapper.setPropertyValue("child.name", "updatedChild");
        wrapper.setPropertyValue("child.age", 25);
        
        assertEquals("updatedChild", child.getName());
        assertEquals(25, child.getAge());
        
        // Test automatic creation of nested objects
        TestBean newParent = new TestBean("newParent", 60);
        BeanWrapper newWrapper = new BeanWrapper(newParent);
        
        // Set nested properties, child is null, should be created automatically
        newWrapper.setPropertyValue("child.name", "autoCreatedChild");
        newWrapper.setPropertyValue("child.age", 10);
        
        assertNotNull(newParent.getChild());
        assertEquals("autoCreatedChild", newParent.getChild().getName());
        assertEquals(10, newParent.getChild().getAge());
    }
    
    /**
     * Test Bean class
     */
    public static class TestBean {
        // Using public fields for testing
        public String name;
        public int age;
        private TestBean child;
        
        public TestBean() {
        }
        
        public TestBean(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            this.age = age;
        }
        
        public TestBean getChild() {
            return child;
        }
        
        public void setChild(TestBean child) {
            this.child = child;
        }
    }
}