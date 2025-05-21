package com.minispring.core.io;

import com.minispring.util.ClassUtils;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default resource loader implementation
 * Supports the following resource types:
 * 1. URL resources: http://, https://, file://, ftp://, etc.
 * 2. ClassPath resources: classpath:
 * 3. File system resources: default
 */
public class DefaultResourceLoader implements ResourceLoader {
    
    private ClassLoader classLoader;
    
    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    @Override
    public Resource getResource(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Resource location cannot be empty");
        }
        
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // Load classpath resource
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        }
        
        try {
            // Try to load as URL resource
            URL url = new URL(location);
            return new UrlResource(url);
        } catch (MalformedURLException ex) {
            // Load as file system resource
            return new FileSystemResource(location);
        }
    }
    
    @Override
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
    }
    
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
} 