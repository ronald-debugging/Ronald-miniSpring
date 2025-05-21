package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Listable Bean Factory Implementation
 * Supports singleton and prototype beans, and provides bean definition registration functionality
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    /**
     * BeanDefinition container
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    
    /**
     * Parent BeanFactory
     */
    private BeanFactory parentBeanFactory;
    
    /**
     * Scope container
     */
    private final Map<String, Scope> scopes = new ConcurrentHashMap<>(8);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No BeanDefinition found for bean named '" + beanName + "'");
        }
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    /**
     * Get bean names for a specific type
     * @param type bean type
     * @return array of bean names
     */
    public String[] getBeanNamesForType(Class<?> type) {
        return beanDefinitionMap.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getValue().getBeanClass()))
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }
    
    /**
     * Get bean instance by type
     * @param requiredType bean type
     * @param <T> bean type
     * @return bean instance
     * @throws BeansException if bean retrieval fails
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        String[] beanNames = getBeanNamesForType(requiredType);
        if (beanNames.length == 0) {
            throw new BeansException("No bean found of type '" + requiredType.getName() + "'");
        }
        if (beanNames.length > 1) {
            throw new BeansException("Found multiple beans of type '" + requiredType.getName() + "': " + String.join(", ", beanNames));
        }
        return getBean(beanNames[0], requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            Class<?> beanClass = beanDefinition.getBeanClass();
            
            if (type.isAssignableFrom(beanClass)) {
                T bean = (T) getBean(beanName);
                result.put(beanName, bean);
            }
        }
        return result;
    }
    
    @Override
    public void preInstantiateSingletons() throws BeansException {
        // Pre-instantiate all non-lazy singleton beans
        for (String beanName : getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if (beanDefinition.isSingleton()) {
                getBean(beanName);
            }
        }
    }
    
    @Override
    public void autowireBean(Object existingBean, String beanName) throws BeansException {
        // Simple implementation, actual autowiring is more complex
        System.out.println("Autowiring bean: " + beanName);
    }
    
    @Override
    public Object createBean(Class<?> beanClass) throws BeansException {
        // Simple implementation, creates a new bean instance
        try {
            return beanClass.newInstance();
        } catch (Exception e) {
            throw new BeansException("Failed to create bean instance of type: " + beanClass.getName(), e);
        }
    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }
    
    /**
     * Set parent BeanFactory
     * 
     * @param parentBeanFactory parent BeanFactory
     */
    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override
    public boolean containsLocalBean(String name) {
        return containsBeanDefinition(name);
    }
    
    @Override
    public Class<?> getType(String name) throws BeansException {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition != null) {
            return beanDefinition.getBeanClass();
        }
        
        // If not found in current factory, try to get from parent factory
        if (this.parentBeanFactory != null) {
            if (this.parentBeanFactory instanceof ConfigurableListableBeanFactory) {
                return ((ConfigurableListableBeanFactory) this.parentBeanFactory).getType(name);
            }
        }
        
        throw new BeansException("No bean definition found for bean named '" + name + "'");
    }
    
    @Override
    public void registerScope(String scopeName, Scope scope) {
        this.scopes.put(scopeName, scope);
    }
    
    @Override
    public Scope getRegisteredScope(String scopeName) {
        return this.scopes.get(scopeName);
    }
} 