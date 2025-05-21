package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Document;

/**
 * Bean Definition Document Reader Interface
 * Used to read bean definitions from XML document and register them to BeanDefinitionRegistry
 */
public interface BeanDefinitionDocumentReader {
    
    /**
     * Read bean definitions from Document
     * 
     * @param document XML document to parse
     * @param registry bean definition registry to register parsed bean definitions
     * @throws BeansException if an error occurs during parsing
     */
    void registerBeanDefinitions(Document document, BeanDefinitionRegistry registry) throws BeansException;
} 