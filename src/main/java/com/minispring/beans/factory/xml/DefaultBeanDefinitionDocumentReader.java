package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;

import java.util.List;

/**
 * Default Bean Definition Document Reader Implementation
 * Used to read bean definitions from XML document and register them to BeanDefinitionRegistry
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

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
     * Namespace handler resolver
     */
    private NamespaceHandlerResolver namespaceHandlerResolver;
    
    /**
     * Default constructor
     */
    public DefaultBeanDefinitionDocumentReader() {
        this.namespaceHandlerResolver = new DefaultNamespaceHandlerResolver();
    }
    
    /**
     * Constructor
     * 
     * @param namespaceHandlerResolver namespace handler resolver
     */
    public DefaultBeanDefinitionDocumentReader(NamespaceHandlerResolver namespaceHandlerResolver) {
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }
    
    /**
     * Set namespace handler resolver
     * 
     * @param namespaceHandlerResolver namespace handler resolver
     */
    public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver) {
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }

    /**
     * Read bean definitions from Document
     * 
     * @param document XML document to parse
     * @param registry bean definition registry to register parsed bean definitions
     * @throws BeansException if an error occurs during parsing
     */
    @Override
    public void registerBeanDefinitions(Document document, BeanDefinitionRegistry registry) throws BeansException {
        // Get root element
        Element root = document.getRootElement();
        
        // Parse bean definitions in the document
        doRegisterBeanDefinitions(root, registry);
    }

    /**
     * Parse bean definitions starting from root element
     * 
     * @param root root element
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    protected void doRegisterBeanDefinitions(Element root, BeanDefinitionRegistry registry) throws BeansException {
        // Process all child elements under root
        List<Element> elements = root.elements();
        for (Element element : elements) {
            // Get namespace of the element
            String namespaceUri = element.getNamespaceURI();
            
            if (namespaceUri != null && !namespaceUri.isEmpty()) {
                // Handle custom namespace
                parseCustomElement(element, registry);
            } else if (element.getName().equals(BEAN_ELEMENT)) {
                // Handle default bean element
                processBeanDefinition(element, registry);
            }
        }
    }
    
    /**
     * Parse custom namespace element
     * 
     * @param element element to parse
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    protected void parseCustomElement(Element element, BeanDefinitionRegistry registry) throws BeansException {
        String namespaceUri = element.getNamespaceURI();
        NamespaceHandler handler = namespaceHandlerResolver.resolve(namespaceUri);
        
        if (handler == null) {
            throw new XmlBeanDefinitionStoreException("No handler found for namespace [" + namespaceUri + "]");
        }
        
        handler.parse(element, registry);
    }

    /**
     * Parse a single bean element and register to registry
     * 
     * @param beanElement bean element
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    protected void processBeanDefinition(Element beanElement, BeanDefinitionRegistry registry) throws BeansException {
        // Parse attributes of bean element
        String id = beanElement.attributeValue(ID_ATTRIBUTE);
        String name = beanElement.attributeValue(NAME_ATTRIBUTE);
        String className = beanElement.attributeValue(CLASS_ATTRIBUTE);
        String initMethodName = beanElement.attributeValue(INIT_METHOD_ATTRIBUTE);
        String destroyMethodName = beanElement.attributeValue(DESTROY_METHOD_ATTRIBUTE);
        String scope = beanElement.attributeValue(SCOPE_ATTRIBUTE);

        // Get Class object
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new XmlBeanDefinitionStoreException("Class not found [" + className + "]", e);
        }

        // Determine bean name
        String beanName = id != null && !id.isEmpty() ? id : name;
        if (beanName == null || beanName.isEmpty()) {
            // If neither id nor name is specified, use class name with first letter in lowercase as bean name
            beanName = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
        }

        // Create BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(clazz);

        // Set init and destroy methods
        if (initMethodName != null && !initMethodName.isEmpty()) {
            beanDefinition.setInitMethodName(initMethodName);
        }
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            beanDefinition.setDestroyMethodName(destroyMethodName);
        }

        // Set scope
        if (scope != null && !scope.isEmpty()) {
            beanDefinition.setScope(scope);
        }

        // Parse property elements
        parsePropertyElements(beanElement, beanDefinition);
        
        // Handle custom attributes
        parseCustomAttributes(beanElement, beanDefinition, registry);

        // Register BeanDefinition
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
    
    /**
     * Parse custom attributes
     * 
     * @param element element containing attributes
     * @param beanDefinition bean definition
     * @param registry bean definition registry
     * @throws BeansException if an error occurs during parsing
     */
    protected void parseCustomAttributes(Element element, BeanDefinition beanDefinition, BeanDefinitionRegistry registry) throws BeansException {
        List<org.dom4j.Attribute> attributes = element.attributes();
        for (org.dom4j.Attribute attribute : attributes) {
            String namespaceUri = attribute.getNamespaceURI();
            if (namespaceUri != null && !namespaceUri.isEmpty()) {
                NamespaceHandler handler = namespaceHandlerResolver.resolve(namespaceUri);
                if (handler != null) {
                    handler.decorate(element, attribute.getName(), registry);
                }
            }
        }
    }

    /**
     * Parse property elements in bean element
     * 
     * @param beanElement bean element
     * @param beanDefinition bean definition
     * @throws BeansException if an error occurs during parsing
     */
    protected void parsePropertyElements(Element beanElement, BeanDefinition beanDefinition) throws BeansException {
        List<Element> propertyElements = beanElement.elements(PROPERTY_ELEMENT);
        PropertyValues propertyValues = new PropertyValues();
        
        for (Element propertyElement : propertyElements) {
            parsePropertyElement(propertyElement, propertyValues);
        }
        
        beanDefinition.setPropertyValues(propertyValues);
    }

    /**
     * Parse a single property element
     * 
     * @param propertyElement property element
     * @param propertyValues property values collection
     * @throws BeansException if an error occurs during parsing
     */
    protected void parsePropertyElement(Element propertyElement, PropertyValues propertyValues) throws BeansException {
        String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);
        String propertyValue = propertyElement.attributeValue(VALUE_ATTRIBUTE);
        String propertyRef = propertyElement.attributeValue(REF_ATTRIBUTE);

        if (propertyName == null || propertyName.isEmpty()) {
            throw new XmlBeanDefinitionStoreException("The 'name' attribute must be specified for bean property element");
        }

        Object value;
        if (propertyValue != null && !propertyValue.isEmpty()) {
            // Normal property value
            value = propertyValue;
        } else if (propertyRef != null && !propertyRef.isEmpty()) {
            // Reference to another bean
            value = new BeanReference(propertyRef);
        } else {
            throw new XmlBeanDefinitionStoreException("Either 'value' or 'ref' attribute must be specified for bean property element");
        }

        PropertyValue pv = new PropertyValue(propertyName, value);
        propertyValues.addPropertyValue(pv);
    }
} 