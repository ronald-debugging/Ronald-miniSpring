package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;

/**
 * XML Bean Definition Store Exception
 * Used to handle exceptions during XML parsing
 */
public class XmlBeanDefinitionStoreException extends BeansException {

    /**
     * Constructor
     * @param message exception message
     */
    public XmlBeanDefinitionStoreException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message exception message
     * @param cause original exception
     */
    public XmlBeanDefinitionStoreException(String message, Throwable cause) {
        super(message, cause);
    }
} 