package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Circular dependency test class
 * Test the ability to resolve circular dependencies using three-level cache
 */
public class CircularDependencyTest {

    /**
     * Test mutual dependency scenario
     * A depends on B, B depends on A
     */
    @Test
    public void testCircularDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register Bean definition for A
        BeanDefinition beanDefinitionA = new BeanDefinition(TestServiceA.class);
        PropertyValues propertyValuesA = new PropertyValues();
        propertyValuesA.addPropertyValue(new PropertyValue("serviceB", new BeanReference("serviceB")));
        beanDefinitionA.setPropertyValues(propertyValuesA);
        beanFactory.registerBeanDefinition("serviceA", beanDefinitionA);
        
        // Register Bean definition for B
        BeanDefinition beanDefinitionB = new BeanDefinition(TestServiceB.class);
        PropertyValues propertyValuesB = new PropertyValues();
        propertyValuesB.addPropertyValue(new PropertyValue("serviceA", new BeanReference("serviceA")));
        beanDefinitionB.setPropertyValues(propertyValuesB);
        beanFactory.registerBeanDefinition("serviceB", beanDefinitionB);
        
        // Get A, trigger circular dependency resolution
        TestServiceA serviceA = (TestServiceA) beanFactory.getBean("serviceA");
        TestServiceB serviceB = (TestServiceB) beanFactory.getBean("serviceB");
        
        // Verify circular dependency is successfully resolved
        assertNotNull(serviceA);
        assertNotNull(serviceB);
        assertNotNull(serviceA.getServiceB());
        assertNotNull(serviceB.getServiceA());
        assertSame(serviceA, serviceB.getServiceA());
        assertSame(serviceB, serviceA.getServiceB());
        
        System.out.println("Circular dependency test passed:");
        System.out.println("ServiceA: " + serviceA + ", ServiceB: " + serviceB);
        System.out.println("ServiceA.serviceB: " + serviceA.getServiceB());
        System.out.println("ServiceB.serviceA: " + serviceB.getServiceA());
    }

    /**
     * Test service A
     */
    public static class TestServiceA {
        private TestServiceB serviceB;
        
        public TestServiceA() {
            System.out.println("Creating TestServiceA instance");
        }
        
        public void setServiceB(TestServiceB serviceB) {
            System.out.println("Setting TestServiceA.serviceB = " + serviceB);
            this.serviceB = serviceB;
        }
        
        public TestServiceB getServiceB() {
            return serviceB;
        }
        
        @Override
        public String toString() {
            return "TestServiceA@" + Integer.toHexString(hashCode());
        }
    }
    
    /**
     * Test service B
     */
    public static class TestServiceB {
        private TestServiceA serviceA;
        
        public TestServiceB() {
            System.out.println("Creating TestServiceB instance");
        }
        
        public void setServiceA(TestServiceA serviceA) {
            System.out.println("Setting TestServiceB.serviceA = " + serviceA);
            this.serviceA = serviceA;
        }
        
        public TestServiceA getServiceA() {
            return serviceA;
        }
        
        @Override
        public String toString() {
            return "TestServiceB@" + Integer.toHexString(hashCode());
        }
    }
    
    /**
     * Test three-level circular dependency
     * A depends on B, B depends on C, C depends on A
     */
    @Test
    public void testThreeLevelCircularDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Register Bean definition for A
        BeanDefinition beanDefinitionA = new BeanDefinition(ServiceA.class);
        PropertyValues propertyValuesA = new PropertyValues();
        propertyValuesA.addPropertyValue(new PropertyValue("serviceB", new BeanReference("serviceB")));
        beanDefinitionA.setPropertyValues(propertyValuesA);
        beanFactory.registerBeanDefinition("serviceA", beanDefinitionA);
        
        // Register Bean definition for B
        BeanDefinition beanDefinitionB = new BeanDefinition(ServiceB.class);
        PropertyValues propertyValuesB = new PropertyValues();
        propertyValuesB.addPropertyValue(new PropertyValue("serviceC", new BeanReference("serviceC")));
        beanDefinitionB.setPropertyValues(propertyValuesB);
        beanFactory.registerBeanDefinition("serviceB", beanDefinitionB);
        
        // Register Bean definition for C
        BeanDefinition beanDefinitionC = new BeanDefinition(ServiceC.class);
        PropertyValues propertyValuesC = new PropertyValues();
        propertyValuesC.addPropertyValue(new PropertyValue("serviceA", new BeanReference("serviceA")));
        beanDefinitionC.setPropertyValues(propertyValuesC);
        beanFactory.registerBeanDefinition("serviceC", beanDefinitionC);
        
        // Get A, trigger circular dependency resolution
        ServiceA serviceA = (ServiceA) beanFactory.getBean("serviceA");
        ServiceB serviceB = (ServiceB) beanFactory.getBean("serviceB");
        ServiceC serviceC = (ServiceC) beanFactory.getBean("serviceC");
        
        // Verify circular dependency is successfully resolved
        assertNotNull(serviceA);
        assertNotNull(serviceB);
        assertNotNull(serviceC);
        assertNotNull(serviceA.getServiceB());
        assertNotNull(serviceB.getServiceC());
        assertNotNull(serviceC.getServiceA());
        
        // Verify reference relationships
        assertSame(serviceA, serviceC.getServiceA());
        assertSame(serviceB, serviceA.getServiceB());
        assertSame(serviceC, serviceB.getServiceC());
        
        System.out.println("Three-level circular dependency test passed");
    }
    
    /**
     * Service A
     */
    public static class ServiceA {
        private ServiceB serviceB;
        
        public ServiceB getServiceB() {
            return serviceB;
        }
        
        public void setServiceB(ServiceB serviceB) {
            this.serviceB = serviceB;
        }
    }
    
    /**
     * Service B
     */
    public static class ServiceB {
        private ServiceC serviceC;
        
        public ServiceC getServiceC() {
            return serviceC;
        }
        
        public void setServiceC(ServiceC serviceC) {
            this.serviceC = serviceC;
        }
    }
    
    /**
     * Service C
     */
    public static class ServiceC {
        private ServiceA serviceA;
        
        public ServiceA getServiceA() {
            return serviceA;
        }
        
        public void setServiceA(ServiceA serviceA) {
            this.serviceA = serviceA;
        }
    }
} 