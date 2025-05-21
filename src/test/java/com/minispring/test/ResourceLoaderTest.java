package com.minispring.test;

import com.minispring.core.io.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Resource loader test class
 */
public class ResourceLoaderTest {
    
    private ResourceLoader resourceLoader;
    private Path resourcesDir;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() throws IOException {
        resourceLoader = new DefaultResourceLoader();
        
        // Create test resources directory
        resourcesDir = tempDir.resolve("test-resources");
        Files.createDirectories(resourcesDir);
        
        // Create test resource file
        Path testFile = resourcesDir.resolve("test.txt");
        Files.write(testFile, "Hello, MiniSpring!".getBytes(StandardCharsets.UTF_8));
        
        // Create custom class loader
        URL[] urls = new URL[]{resourcesDir.toUri().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);
        resourceLoader = new DefaultResourceLoader(classLoader);
    }
    
    @Test
    void testClassPathResource() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:test.txt");
        assertTrue(resource instanceof ClassPathResource);
        assertTrue(resource.isReadable());
        
        // Verify resource content
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals("Hello, MiniSpring!", reader.readLine());
        }
    }
    
    @Test
    void testFileSystemResource() throws IOException {
        // Create test file
        String content = "File System Resource Test";
        Path filePath = tempDir.resolve("fs-test.txt");
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        
        // Test file system resource
        Resource resource = resourceLoader.getResource(filePath.toString());
        assertTrue(resource instanceof FileSystemResource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        
        // Verify resource content
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals(content, reader.readLine());
        }
        
        // Test non-existent file
        Resource nonExistentResource = resourceLoader.getResource(tempDir.resolve("non-existent.txt").toString());
        assertFalse(nonExistentResource.exists());
    }
    
    @Test
    void testUrlResource() throws IOException {
        // Test HTTP URL resource
        Resource resource = resourceLoader.getResource("https://www.baidu.com");
        assertTrue(resource instanceof UrlResource);
        assertTrue(resource.isReadable());
        
        // Verify resource content (only verify that input stream can be obtained)
        try (InputStream is = resource.getInputStream()) {
            assertNotNull(is);
            assertTrue(is.read() > -1);
        }
        
        // Test invalid URL
        Resource invalidResource = resourceLoader.getResource("https://invalid.example.com");
        assertFalse(invalidResource.exists());
    }
    
    @Test
    void testResourceDescription() {
        // Test description information for various resources
        Resource classpathResource = resourceLoader.getResource("classpath:test.txt");
        assertTrue(classpathResource.getDescription().contains("Classpath resource"));
        
        Resource fileResource = resourceLoader.getResource("/path/to/file.txt");
        assertTrue(fileResource.getDescription().contains("File system resource"));
        
        Resource urlResource = resourceLoader.getResource("https://www.example.com");
        assertTrue(urlResource.getDescription().contains("URL resource"));
    }
    
    @Test
    void testNullAndEmptyLocations() {
        // Test empty paths
        assertThrows(IllegalArgumentException.class, () -> resourceLoader.getResource(null));
        assertThrows(IllegalArgumentException.class, () -> resourceLoader.getResource(""));
    }
    
    @Test
    void testCustomClassLoader() {
        // Test custom class loader
        ClassLoader customClassLoader = new URLClassLoader(new URL[0]);
        ResourceLoader customLoader = new DefaultResourceLoader(customClassLoader);
        assertEquals(customClassLoader, customLoader.getClassLoader());
    }
    
    @Test
    void testRelativeFileSystemResource() throws IOException {
        // Create test directory structure
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        System.out.println("Subdirectory path: " + subDir.toAbsolutePath());
        
        // Create test file
        String content = "Test Content";
        Path testFile = subDir.resolve("test.txt");
        Files.write(testFile, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("Test file path: " + testFile.toAbsolutePath());
        
        // Test relative path resource
        FileSystemResource baseResource = new FileSystemResource(subDir.toFile());
        System.out.println("Base resource path: " + baseResource.getPath());
        assertTrue(baseResource.exists(), "Base directory should exist");
        
        Resource relativeResource = baseResource.createRelative("test.txt");
        System.out.println("Relative resource path: " + ((FileSystemResource)relativeResource).getPath());
        System.out.println("Relative resource exists: " + relativeResource.exists());
        System.out.println("Relative resource file exists: " + ((FileSystemResource)relativeResource).getFile().exists());
        assertTrue(relativeResource.exists(), "Relative path resource should exist");
        
        // Verify resource content
        try (InputStream is = relativeResource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals(content, reader.readLine(), "Resource content should match");
        }
        
        // Test non-existent relative path
        Resource nonExistentResource = baseResource.createRelative("non-existent.txt");
        assertFalse(nonExistentResource.exists(), "Non-existent resource should return false");
    }
} 