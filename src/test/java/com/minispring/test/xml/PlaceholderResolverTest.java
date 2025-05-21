package com.minispring.test.xml;

import com.minispring.beans.factory.config.PropertyPlaceholderResolver;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Placeholder resolver test class
 */
public class PlaceholderResolverTest {
    
    @Test
    void testResolvePlaceholders() {
        // Create properties
        Properties properties = new Properties();
        properties.setProperty("name", "Zhang San");
        properties.setProperty("age", "30");
        properties.setProperty("city", "Beijing");
        
        // Create placeholder resolver
        PropertyPlaceholderResolver resolver = new PropertyPlaceholderResolver(properties);
        
        // Test resolving single placeholder
        String value1 = resolver.resolvePlaceholders("Hello, ${name}!");
        assertEquals("Hello, Zhang San!", value1);
        
        // Test resolving multiple placeholders
        String value2 = resolver.resolvePlaceholders("${name}今年${age}岁，住在${city}");
        assertEquals("Zhang San今年30岁，住在Beijing", value2);
        
        // Test resolving placeholder with default value
        String value3 = resolver.resolvePlaceholders("${name}的工作是${job:程序员}");
        assertEquals("Zhang San的工作是程序员", value3);
        
        // Test resolving non-existent placeholder
        String value4 = resolver.resolvePlaceholders("${name}的爱好是${hobby}");
        assertEquals("Zhang San的爱好是${hobby}", value4);
        
        // Test checking if string contains placeholder
        assertTrue(resolver.containsPlaceholder("Hello, ${name}!"));
    }
}