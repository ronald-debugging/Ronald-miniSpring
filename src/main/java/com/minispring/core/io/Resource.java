package com.minispring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource access interface
 * Defines basic operations for resources
 */
public interface Resource {
    
    /**
     * Check if the resource exists
     * @return whether the resource exists
     */
    boolean exists();
    
    /**
     * Check if the resource is readable
     * @return whether the resource is readable
     */
    boolean isReadable();
    
    /**
     * Get the input stream of the resource
     * @return resource input stream
     * @throws IOException if the input stream cannot be obtained
     */
    InputStream getInputStream() throws IOException;
    
    /**
     * Get the description of the resource
     * @return resource description
     */
    String getDescription();
} 