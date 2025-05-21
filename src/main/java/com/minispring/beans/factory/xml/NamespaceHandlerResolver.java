package com.minispring.beans.factory.xml;

/**
 * Namespace Handler Resolver Interface
 * Used to resolve namespace handlers based on namespace URI
 */
public interface NamespaceHandlerResolver {
    
    /**
     * Resolve namespace handler based on namespace URI
     * 
     * @param namespaceUri namespace URI
     * @return corresponding namespace handler, returns null if not found
     */
    NamespaceHandler resolve(String namespaceUri);
} 