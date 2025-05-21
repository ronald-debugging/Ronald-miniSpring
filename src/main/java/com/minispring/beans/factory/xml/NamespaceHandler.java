package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Element;

/**
 * Namespace Handler Interface
 * Used to handle custom namespaces in XML
 */
public interface NamespaceHandler {
    
    /**
     * Initialize the namespace handler
     */
    void init();
    
    /**
     * Parse namespace element
     * 
     * @param element element to parse
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    void parse(Element element, BeanDefinitionRegistry registry) throws BeansException;
    
    /**
     * Parse custom attribute
     * 
     * @param element element containing the attribute
     * @param attributeName attribute name
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException;
} 