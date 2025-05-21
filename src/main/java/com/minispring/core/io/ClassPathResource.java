package com.minispring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classpath resource implementation
 */
public class ClassPathResource implements Resource {
    
    private final String path;
    private final ClassLoader classLoader;
    
    public ClassPathResource(String path) {
        this(path, null);
    }
    
    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        this.path = path.startsWith("/") ? path.substring(1) : path;
        this.classLoader = classLoader != null ? classLoader : getDefaultClassLoader();
    }
    
    @Override
    public boolean exists() {
        return classLoader.getResource(path) != null;
    }
    
    @Override
    public boolean isReadable() {
        return true;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("Classpath resource [" + path + "] does not exist");
        }
        return is;
    }
    
    @Override
    public String getDescription() {
        return "Classpath resource [" + path + "]";
    }
    
    public String getPath() {
        return path;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /**
     * Get the default class loader
     */
    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Ignore when unable to get thread context class loader
        }
        if (cl == null) {
            cl = ClassPathResource.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Ignore when unable to get system class loader
                }
            }
        }
        return cl;
    }
} 