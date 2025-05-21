package com.minispring.beans.factory.config;

import com.minispring.beans.factory.ConfigurableBeanFactory;
import com.minispring.web.context.request.RequestScope;
import com.minispring.web.context.request.SessionScope;

/**
 * Scope Registrar
 * Used to register various scopes with the bean factory
 */
public class ScopeRegistrar {
    
    /**
     * Register standard scopes (singleton and prototype)
     * @param beanFactory target bean factory
     */
    public static void registerStandardScopes(ConfigurableBeanFactory beanFactory) {
        // Register singleton scope
        beanFactory.registerScope(ConfigurableBeanFactory.SCOPE_SINGLETON, new SingletonScope());
        
        // Register prototype scope
        beanFactory.registerScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE, new PrototypeScope());
    }
    
    /**
     * Register web-related scopes
     * @param beanFactory target bean factory
     */
    public static void registerWebScopes(ConfigurableBeanFactory beanFactory) {
        // Register request scope
        beanFactory.registerScope("request", new RequestScope());
        
        // Register session scope
        beanFactory.registerScope("session", new SessionScope());
    }
    
    /**
     * Register all scopes
     * @param beanFactory target bean factory
     */
    public static void registerAllScopes(ConfigurableBeanFactory beanFactory) {
        registerStandardScopes(beanFactory);
        
        // Can determine whether to register web scopes based on environment type
        try {
            // Check if web-related classes exist
            Class.forName("javax.servlet.http.HttpServletRequest");
            // If they exist, register web scopes
            registerWebScopes(beanFactory);
        } catch (ClassNotFoundException e) {
            // Not a web environment, ignore
        }
    }
} 