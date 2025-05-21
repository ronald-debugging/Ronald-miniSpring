package com.minispring.test.environment;

import com.minispring.core.env.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Environment test class
 */
public class EnvironmentTest {
    
    /**
     * Test basic functionality of StandardEnvironment
     */
    @Test
    public void testStandardEnvironment() {
        StandardEnvironment environment = new StandardEnvironment();
        
        // Test system properties
        System.setProperty("test.property", "test-value");
        assertEquals("test-value", environment.getProperty("test.property"));
        
        // Test different types of property retrieval
        System.setProperty("test.int", "123");
        System.setProperty("test.boolean", "true");
        
        assertEquals(123, environment.getProperty("test.int", Integer.class).intValue());
        assertTrue(environment.getProperty("test.boolean", Boolean.class));
        
        // Test default values
        assertEquals("default-value", environment.getProperty("non.existent.property", "default-value"));
        assertEquals(456, environment.getProperty("non.existent.int", Integer.class, 456).intValue());
        
        // Clean up system properties
        System.clearProperty("test.property");
        System.clearProperty("test.int");
        System.clearProperty("test.boolean");
    }
    
    /**
     * Test Profiles functionality
     */
    @Test
    public void testProfiles() {
        AbstractEnvironment environment = new StandardEnvironment();
        
        // By default, only default profile is active
        assertTrue(environment.acceptsProfiles("default"));
        assertFalse(environment.acceptsProfiles("test"));
        
        // Set active profiles
        environment.setActiveProfiles("test", "dev");
        
        assertTrue(environment.acceptsProfiles("test"));
        assertTrue(environment.acceptsProfiles("dev"));
        assertFalse(environment.acceptsProfiles("prod"));
        
        // Test multiple profiles
        assertTrue(environment.acceptsProfiles("prod", "test"));  // Returns true if any profile matches
        
        // Add profile
        environment.addActiveProfile("prod");
        assertTrue(environment.acceptsProfiles("prod"));
    }
    
    /**
     * Test PropertySource precedence
     */
    @Test
    public void testPropertySourcePrecedence() {
        MutablePropertySources propertySources = new MutablePropertySources();
        
        // Add two property sources, first one has higher precedence than second
        MapPropertySource firstSource = new MapPropertySource("first", java.util.Collections.singletonMap("test.key", "first-value"));
        MapPropertySource secondSource = new MapPropertySource("second", java.util.Collections.singletonMap("test.key", "second-value"));
        
        propertySources.addFirst(secondSource);
        propertySources.addFirst(firstSource);
        
        // Create custom environment implementation to test PropertySource precedence
        AbstractEnvironment environment = new AbstractEnvironment() {
            @Override
            protected void customizePropertySources(MutablePropertySources propertySources) {
                propertySources.addFirst(firstSource);
                propertySources.addLast(secondSource);
            }
        };
        
        // Should return value from first property source
        assertEquals("first-value", environment.getProperty("test.key"));
    }
    
    /**
     * Test SystemEnvironmentPropertySource
     */
    @Test
    public void testSystemEnvironmentPropertySource() {
        // Create a mock environment variable Map
        java.util.Map<String, Object> mockEnv = new java.util.HashMap<>();
        mockEnv.put("TEST_ENV_VAR", "test-value");
        mockEnv.put("NESTED_VALUE", "nested");
        
        SystemEnvironmentPropertySource source = new SystemEnvironmentPropertySource("test", mockEnv);
        
        // Test original format
        assertEquals("test-value", source.getProperty("TEST_ENV_VAR"));
        
        // Test lowercase format
        assertEquals("test-value", source.getProperty("test.env.var"));
        
        // Test non-existent property
        assertNull(source.getProperty("non.existent"));
        
        // Test containsProperty method
        assertTrue(source.containsProperty("test.env.var"));
        assertTrue(source.containsProperty("TEST_ENV_VAR"));
        assertFalse(source.containsProperty("non.existent"));
    }
    
    /**
     * Test Environment merging
     */
    @Test
    public void testEnvironmentMerge() {
        // Create parent environment
        StandardEnvironment parent = new StandardEnvironment();
        parent.setActiveProfiles("parent");
        
        // Add custom property source to parent environment
        MutablePropertySources parentSources = parent.getPropertySources();
        parentSources.addFirst(new MapPropertySource("parentProperties", 
                java.util.Collections.singletonMap("parent.property", "parent-value")));
        
        // Create child environment
        StandardEnvironment child = new StandardEnvironment();
        child.setActiveProfiles("child");
        
        // Add custom property source to child environment
        MutablePropertySources childSources = child.getPropertySources();
        childSources.addFirst(new MapPropertySource("childProperties", 
                java.util.Collections.singletonMap("child.property", "child-value")));
        
        // Merge parent environment into child environment
        child.merge(parent);
        
        // Test merged profiles
        assertTrue(child.acceptsProfiles("parent"));
        assertTrue(child.acceptsProfiles("child"));
        
        // Test merged properties
        assertEquals("child-value", child.getProperty("child.property"));
        assertEquals("parent-value", child.getProperty("parent.property"));
    }
} 