package com.minispring.beans.factory;

/**
 * Bean Initialization Interface
 * Beans implementing this interface will execute afterPropertiesSet method after all properties are set
 * This is an important extension point in Spring lifecycle
 */
public interface InitializingBean {
    
    /**
     * Called after all bean properties have been set
     * Can perform custom initialization logic in this method
     * 
     * @throws Exception exceptions that may be thrown during initialization
     */
    void afterPropertiesSet() throws Exception;
} 