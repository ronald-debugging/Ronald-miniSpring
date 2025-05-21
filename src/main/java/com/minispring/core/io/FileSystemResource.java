package com.minispring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File system resource implementation
 */
public class FileSystemResource implements Resource {
    
    private final String path;
    private final File file;
    
    public FileSystemResource(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        this.path = path;
        this.file = new File(path).getAbsoluteFile();
    }
    
    public FileSystemResource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        this.path = file.getPath();
        this.file = file.getAbsoluteFile();
    }
    
    @Override
    public boolean exists() {
        return file.exists();
    }
    
    @Override
    public boolean isReadable() {
        return file.canRead();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return new FileInputStream(file);
        } catch (IOException ex) {
            throw new IOException("Cannot open file [" + path + "]", ex);
        }
    }
    
    @Override
    public String getDescription() {
        return "File system resource [" + path + "]";
    }
    
    public String getPath() {
        return path;
    }
    
    public File getFile() {
        return file;
    }
    
    /**
     * Get the last modified time of the file
     */
    public long lastModified() throws IOException {
        return Files.getLastModifiedTime(file.toPath()).toMillis();
    }
    
    /**
     * Create a new resource relative to this resource
     */
    public Resource createRelative(String relativePath) {
        if (relativePath == null) {
            throw new IllegalArgumentException("Relative path cannot be null");
        }
        
        try {
            // Get the current file path
            Path basePath = file.toPath();
            
            // If current path is a file, use its parent directory as base path
            if (Files.isRegularFile(basePath)) {
                basePath = basePath.getParent();
            }
            
            // Resolve relative path
            Path resolvedPath = basePath.resolve(relativePath).normalize();
            return new FileSystemResource(resolvedPath.toFile());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot create relative path resource [" + relativePath + "]", ex);
        }
    }
} 