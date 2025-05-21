package com.minispring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * Default XML Document Loader Implementation
 * Uses DOM4J's SAXReader to parse XML documents
 */
public class DefaultDocumentLoader implements DocumentLoader {

    /**
     * Load XML document from input stream
     * 
     * @param inputStream XML input stream
     * @return parsed Document object
     * @throws DocumentException if an error occurs during parsing
     */
    @Override
    public Document loadDocument(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(inputStream);
    }
} 