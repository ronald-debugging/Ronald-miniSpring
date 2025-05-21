package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.AbstractBeanDefinitionReader;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import com.minispring.core.io.Resource;
import com.minispring.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * XML Bean Definition Reader
 * Used to read bean definitions from XML files
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private DocumentLoader documentLoader = new DefaultDocumentLoader();
    private BeanDefinitionDocumentReader beanDefinitionDocumentReader = new DefaultBeanDefinitionDocumentReader();

    /**
     * XML tag and attribute constants
     */
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";

    /**
     * Constructor
     * @param registry bean definition registry
     */
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * Constructor
     * @param registry bean definition registry
     * @param resourceLoader resource loader
     */
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    /**
     * Set document loader
     * @param documentLoader document loader
     */
    public void setDocumentLoader(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    /**
     * Set bean definition document reader
     * @param beanDefinitionDocumentReader bean definition document reader
     */
    public void setBeanDefinitionDocumentReader(BeanDefinitionDocumentReader beanDefinitionDocumentReader) {
        this.beanDefinitionDocumentReader = beanDefinitionDocumentReader;
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream, resource);
        } catch (IOException | DocumentException e) {
            throw new XmlBeanDefinitionStoreException("Failed to parse XML file [" + resource + "]", e);
        }
    }

    /**
     * Load bean definitions from input stream
     * @param inputStream input stream
     * @param resource resource (for error reporting)
     * @throws DocumentException XML parsing exception
     */
    protected void doLoadBeanDefinitions(InputStream inputStream, Resource resource) throws DocumentException {
        // Use DocumentLoader to load XML document
        Document document = documentLoader.loadDocument(inputStream);
        
        // Use BeanDefinitionDocumentReader to register bean definitions
        beanDefinitionDocumentReader.registerBeanDefinitions(document, getRegistry());
        
        // Log message
        System.out.println("Loaded bean definitions from resource [" + resource + "]");
    }
} 