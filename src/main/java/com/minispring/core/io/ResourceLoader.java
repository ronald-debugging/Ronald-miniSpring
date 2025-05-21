package com.minispring.core.io;

/**
 * Resource loader interface
 * Defines unified method for obtaining resources
 */
public interface ResourceLoader {
    
    /** Classpath URL prefix */
    String CLASSPATH_URL_PREFIX = "classpath:";
    
    /** File system URL prefix */
    String FILE_URL_PREFIX = "file:";
    
    /** HTTP URL prefix */
    String HTTP_URL_PREFIX = "http:";
    
    /** HTTPS URL prefix */
    String HTTPS_URL_PREFIX = "https:";
    
    /**
     * Get resource
     * @param location resource location
     * @return resource object
     */
    Resource getResource(String location);
    
    /**
     * Get class loader
     * @return class loader
     */
    ClassLoader getClassLoader();
} 