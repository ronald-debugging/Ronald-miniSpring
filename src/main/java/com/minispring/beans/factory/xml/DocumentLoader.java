package com.minispring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * XML Document Loader Interface
 * Used to parse XML input stream into Document object
 */
public interface DocumentLoader {
    
    /**
     * Load XML document from input stream
     * 
     * @param inputStream XML input stream
     * @return parsed Document object
     * @throws DocumentException if an error occurs during parsing
     */
    Document loadDocument(InputStream inputStream) throws DocumentException;
} 