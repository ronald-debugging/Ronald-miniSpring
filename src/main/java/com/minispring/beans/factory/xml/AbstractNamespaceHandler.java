package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Namespace Handler
 * Provides basic implementation for namespace handlers
 */
public abstract class AbstractNamespaceHandler implements NamespaceHandler {
    
    /**
     * Element parser map
     */
    private final Map<String, ElementParser> elementParsers = new HashMap<>();
    
    /**
     * Attribute decorator map
     */
    private final Map<String, AttributeDecorator> attributeDecorators = new HashMap<>();
    
    /**
     * Register element parser
     * 
     * @param elementName element name
     * @param parser element parser
     */
    protected void registerElementParser(String elementName, ElementParser parser) {
        elementParsers.put(elementName, parser);
    }
    
    /**
     * Register attribute decorator
     * 
     * @param attributeName attribute name
     * @param decorator attribute decorator
     */
    protected void registerAttributeDecorator(String attributeName, AttributeDecorator decorator) {
        attributeDecorators.put(attributeName, decorator);
    }
    
    @Override
    public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
        String localName = element.getName();
        ElementParser parser = elementParsers.get(localName);
        if (parser != null) {
            parser.parse(element, registry);
        } else {
            throw new XmlBeanDefinitionStoreException("Unknown element [" + localName + "] in namespace [" + element.getNamespaceURI() + "]");
        }
    }
    
    @Override
    public void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException {
        AttributeDecorator decorator = attributeDecorators.get(attributeName);
        if (decorator != null) {
            decorator.decorate(element, attributeName, registry);
        } else {
            throw new XmlBeanDefinitionStoreException("Unknown attribute [" + attributeName + "] in namespace [" + element.getNamespaceURI() + "]");
        }
    }
    
    /**
     * Element parser interface
     */
    public interface ElementParser {
        /**
         * Parse element
         * 
         * @param element element to parse
         * @param registry bean definition registry
         * @throws BeansException if an error occurs during parsing
         */
        void parse(Element element, BeanDefinitionRegistry registry) throws BeansException;
    }
    
    /**
     * Attribute decorator interface
     */
    public interface AttributeDecorator {
        /**
         * Decorate element
         * 
         * @param element element containing the attribute
         * @param attributeName attribute name
         * @param registry bean definition registry
         * @throws BeansException if an error occurs during decoration
         */
        void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException;
    }
}
