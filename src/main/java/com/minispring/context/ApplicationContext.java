package com.minispring.context;

import com.minispring.beans.factory.ListableBeanFactory;
import com.minispring.core.env.Environment;

/**
 * ApplicationContext Interface
 * Spring application context, extends ListableBeanFactory, provides more enterprise-level functionality
 */
public interface ApplicationContext extends ListableBeanFactory {
    
    /**
     * Get application context name
     * @return application context name
     */
    String getApplicationName();
    
    /**
     * Get application context startup time
     * @return startup time (milliseconds)
     */
    long getStartupDate();
    
    /**
     * Get Environment
     * @return Environment
     */
    Environment getEnvironment();
} 