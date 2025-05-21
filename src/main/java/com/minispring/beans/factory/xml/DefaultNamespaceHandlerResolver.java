package com.minispring.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Default Namespace Handler Resolver Implementation
 * Loads mapping from namespace URI to handler class from configuration file
 */
public class DefaultNamespaceHandlerResolver implements NamespaceHandlerResolver {
    
    /**
     * Default handler mappings file path
     */
    public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
    
    /**
     * Mapping from namespace URI to handler class name
     */
    private final Map<String, String> handlerMappings = new HashMap<>();
    
    /**
     * Cache of resolved handlers
     */
    private final Map<String, NamespaceHandler> handlerCache = new HashMap<>();
    
    /**
     * Create resolver using default handler mappings file path
     */
    public DefaultNamespaceHandlerResolver() {
        this(DEFAULT_HANDLER_MAPPINGS_LOCATION);
    }
    
    /**
     * Create resolver using specified handler mappings file path
     * 
     * @param handlerMappingsLocation handler mappings file path
     */
    public DefaultNamespaceHandlerResolver(String handlerMappingsLocation) {
        loadHandlerMappings(handlerMappingsLocation);
    }
    
    /**
     * Load handler mappings from configuration file
     * 
     * @param handlerMappingsLocation handler mappings file path
     */
    private void loadHandlerMappings(String handlerMappingsLocation) {
        try {
            Properties mappings = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream(handlerMappingsLocation);
            if (is != null) {
                try {
                    mappings.load(is);
                } finally {
                    is.close();
                }
                
                for (Map.Entry<Object, Object> entry : mappings.entrySet()) {
                    String namespaceUri = (String) entry.getKey();
                    String handlerClassName = (String) entry.getValue();
                    handlerMappings.put(namespaceUri, handlerClassName);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load namespace handler mappings: " + handlerMappingsLocation, ex);
        }
    }
    
    @Override
    public NamespaceHandler resolve(String namespaceUri) {
        // First check cache
        NamespaceHandler handler = handlerCache.get(namespaceUri);
        if (handler != null) {
            return handler;
        }
        
        // Find handler class name
        String handlerClassName = handlerMappings.get(namespaceUri);
        if (handlerClassName == null) {
            return null;
        }
        
        try {
            // Load handler class
            Class<?> handlerClass = Class.forName(handlerClassName);
            if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
                throw new IllegalStateException("Class [" + handlerClassName + "] is not an implementation of NamespaceHandler");
            }
            
            // Instantiate handler
            handler = (NamespaceHandler) handlerClass.newInstance();
            
            // Initialize handler
            handler.init();
            
            // Cache handler
            handlerCache.put(namespaceUri, handler);
            
            return handler;
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Namespace handler class not found: " + handlerClassName, ex);
        } catch (InstantiationException ex) {
            throw new IllegalStateException("Failed to instantiate namespace handler class: " + handlerClassName, ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Failed to access namespace handler class: " + handlerClassName, ex);
        }
    }
} 