package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Element;

/**
 * Context Namespace Handler
 * Used to handle elements and attributes under the context namespace
 */
public class ContextNamespaceHandler extends AbstractNamespaceHandler {
    
    /**
     * Initialize the namespace handler
     */
    @Override
    public void init() {
        // Register element parsers
        registerElementParser("property-placeholder", new PropertyPlaceholderElementParser());
        registerElementParser("component-scan", new ComponentScanElementParser());
        
        // Register attribute decorator
        registerAttributeDecorator("default-lazy-init", new DefaultLazyInitAttributeDecorator());
    }
    
    /**
     * Property placeholder element parser
     */
    private static class PropertyPlaceholderElementParser implements ElementParser {
        @Override
        public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
            String location = element.attributeValue("location");
            if (location != null && !location.isEmpty()) {
                System.out.println("Parsing property-placeholder element, loading property file: " + location);
                // In actual implementation, property file should be loaded and PropertyPlaceholderConfigurer bean created here
            }
        }
    }
    
    /**
     * Component scan element parser
     */
    private static class ComponentScanElementParser implements ElementParser {
        @Override
        public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
            String basePackage = element.attributeValue("base-package");
            if (basePackage != null && !basePackage.isEmpty()) {
                System.out.println("Parsing component-scan element, scanning package: " + basePackage);
                // In actual implementation, components under the specified package should be scanned and registered as beans here
            }
        }
    }
    
    /**
     * Default lazy-init attribute decorator
     */
    private static class DefaultLazyInitAttributeDecorator implements AttributeDecorator {
        @Override
        public void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException {
            String value = element.attributeValue(attributeName);
            if ("true".equals(value)) {
                System.out.println("Set default lazy-init to true");
                // In actual implementation, all beans' default lazy-init property should be set here
            }
        }
    }
} 