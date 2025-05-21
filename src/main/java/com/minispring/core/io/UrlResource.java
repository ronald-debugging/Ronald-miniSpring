package com.minispring.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL resource implementation
 * Supports accessing resources with various URL protocols, such as http, https, ftp, file, etc.
 */
public class UrlResource implements Resource {
    
    private final URL url;
    
    public UrlResource(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        this.url = url;
    }
    
    public UrlResource(String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        this.url = new URL(url);
    }
    
    @Override
    public boolean exists() {
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setRequestMethod("HEAD");
                int code = httpConn.getResponseCode();
                return (code >= 200 && code < 300);
            }
            // For non-HTTP URLs, try to open input stream
            try (InputStream is = conn.getInputStream()) {
                return true;
            }
        } catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public boolean isReadable() {
        return true;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection conn = url.openConnection();
        try {
            return conn.getInputStream();
        } catch (IOException ex) {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
            throw ex;
        }
    }
    
    @Override
    public String getDescription() {
        return "URL resource [" + url + "]";
    }
    
    public URL getURL() {
        return url;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UrlResource)) {
            return false;
        }
        return this.url.equals(((UrlResource) obj).url);
    }
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
} 