package com.minispring.beans.factory.config;

import com.minispring.aop.framework.AopProxy;
import com.minispring.aop.framework.ProxyFactory;
import com.minispring.aop.framework.TargetSource;
import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.ConfigurableBeanFactory;

/**
 * Scoped Proxy Factory
 * Used to create proxies for scoped beans
 */
public class ScopedProxyFactory {
    
    /**
     * Create a scoped proxy
     * @param targetBean target bean
     * @param targetBeanName bean name
     * @param scopeName scope name
     * @param beanFactory bean factory
     * @return proxy for the scoped bean
     */
    public static Object createScopedProxy(Object targetBean, String targetBeanName, String scopeName, ConfigurableBeanFactory beanFactory) {
        if (targetBean == null) {
            throw new IllegalArgumentException("Target bean must not be null");
        }
        
        // Create proxy factory
        ProxyFactory proxyFactory = new ProxyFactory();
        
        // Set target source as lazy target source to get latest bean from target scope on each call
        proxyFactory.setTargetSource(new ScopedTargetSource(targetBeanName, scopeName, beanFactory));
        
        // Set proxy target class to ensure correct proxying
        if (targetBean.getClass().getInterfaces().length > 0) {
            // Use JDK dynamic proxy if target bean implements interfaces
            proxyFactory.setTargetClass(targetBean.getClass());
        } else {
            // Otherwise use CGLIB proxy
            proxyFactory.setTargetClass(targetBean.getClass());
        }
        
        // Get proxy
        return proxyFactory.getProxy();
    }
    
    /**
     * Lazy loading scoped target source
     */
    private static class ScopedTargetSource implements TargetSource {
        
        private final String targetBeanName;
        private final String scopeName;
        private final ConfigurableBeanFactory beanFactory;
        
        public ScopedTargetSource(String targetBeanName, String scopeName, ConfigurableBeanFactory beanFactory) {
            this.targetBeanName = targetBeanName;
            this.scopeName = scopeName;
            this.beanFactory = beanFactory;
        }
        
        @Override
        public Class<?> getTargetClass() {
            try {
                return beanFactory.getType(this.targetBeanName);
            } catch (BeansException ex) {
                // Return Object.class if unable to get type
                return Object.class;
            }
        }
        
        @Override
        public boolean isStatic() {
            // Return false to indicate target object is not static and needs to be resolved on each call
            return false;
        }
        
        @Override
        public Object getTarget() throws Exception {
            // Get target bean from scope
            Scope scope = beanFactory.getRegisteredScope(this.scopeName);
            if (scope == null) {
                throw new IllegalStateException("No Scope registered for scope name '" + this.scopeName + "'");
            }
            
            // Create object factory
            ObjectFactory<Object> objectFactory = new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    return beanFactory.getBean(targetBeanName);
                }
            };
            
            // Use ObjectFactory to get or create bean from scope
            return scope.get(this.targetBeanName, objectFactory);
        }
        
        @Override
        public void releaseTarget(Object target) throws Exception {
            // No special handling is needed, scopes manage the object's lifecycle on their own.
        }
    }
} 