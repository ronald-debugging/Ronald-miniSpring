package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.BeanWrapper;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.SimpleTypeConverter;
import com.minispring.beans.TypeConverter;
import com.minispring.beans.TypeMismatchException;
import com.minispring.beans.factory.BeanFactoryAware;
import com.minispring.beans.factory.BeanNameAware;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.InitializingBean;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.ConstructorResolver.BeanInstantiationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract Autowire Capable Bean Factory
 * Implements bean creation functionality
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    /**
     * Set instantiation strategy
     * @param instantiationStrategy instantiation strategy
     */
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * Get instantiation strategy
     * @return instantiation strategy
     */
    public InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    /**
     * Create bean instance
     * @param beanName bean name
     * @param beanDefinition bean definition
     * @param args constructor arguments
     * @return bean instance
     * @throws BeansException if bean creation fails
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            // Create bean instance
            bean = createBeanInstance(beanDefinition, beanName, args);
            
            // Handle circular dependency, expose instantiated bean to third-level cache
            // Only singleton beans that allow circular dependency will be exposed early
            if (beanDefinition.isSingleton()) {
                final Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
                System.out.println("Exposing bean [" + beanName + "] to third-level cache");
            }
            
            // Create bean wrapper
            BeanWrapper beanWrapper = new BeanWrapper(bean);
            
            // Populate bean properties
            applyPropertyValues(beanName, bean, beanDefinition, beanWrapper);
            
            // Execute bean initialization methods and BeanPostProcessor pre/post processing
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Failed to create bean: " + beanName, e);
        }
        
        // Register destroy method callback
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        
        // Register singleton bean
        if (beanDefinition.isSingleton()) {
            // After handling FactoryBean and circular dependency, finally add to singleton cache
            // If this bean was exposed early (i.e., circular dependency was resolved), this step will clear the factory object from third-level cache
            registerSingleton(beanName, bean);
        }
        
        return bean;
    }

    /**
     * Create bean instance
     * @param beanDefinition bean definition
     * @param beanName bean name
     * @param args constructor arguments
     * @return bean instance
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        System.out.println("Creating bean instance: " + beanName + ", constructor args: " + (args != null ? args.length : 0));
        
        // Get all constructors
        Constructor<?>[] declaredConstructors = beanDefinition.getBeanClass().getDeclaredConstructors();
        
        // Create constructor resolver
        ConstructorResolver constructorResolver = new ConstructorResolver(this);
        
        // Resolve constructor and arguments
        BeanInstantiationContext instantiationContext = constructorResolver.autowireConstructor(
                beanName, beanDefinition, declaredConstructors, args);
        
        // Use instantiation strategy to create bean instance
        return getInstantiationStrategy().instantiate(
                beanDefinition, beanName, instantiationContext.getConstructor(), instantiationContext.getArgs());
    }

    /**
     * Populate bean properties
     * @param beanName bean name
     * @param bean bean instance
     * @param beanDefinition bean definition
     * @param beanWrapper bean wrapper
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            if (propertyValues.isEmpty()) {
                return;
            }
            
            // Create type converter
            TypeConverter typeConverter = new SimpleTypeConverter();
            
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                
                // Handle bean reference
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                
                // Use BeanWrapper to set property value
                beanWrapper.setPropertyValue(name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Failed to populate bean properties: " + beanName, e);
        }
    }

    /**
     * Initialize bean
     * @param beanName bean name
     * @param bean bean instance
     * @param beanDefinition bean definition
     * @return initialized bean instance
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 0. Handle Aware interfaces
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
        
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        
        // 1. Execute BeanPostProcessor pre-processing
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        
        // 2. Execute initialization method
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Failed to execute bean initialization method: " + beanName, e);
        }
        
        // 3. Execute BeanPostProcessor post-processing
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        
        return wrappedBean;
    }

    /**
     * Execute bean initialization methods
     * @param beanName bean name
     * @param bean bean instance
     * @param beanDefinition bean definition
     * @throws Exception if initialization fails
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1. If bean implements InitializingBean interface, call its afterPropertiesSet method
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
            System.out.println("Executing InitializingBean interface's afterPropertiesSet method for bean [" + beanName + "]");
        }
        
        // 2. If bean defines initialization method, execute it
        String initMethodName = beanDefinition.getInitMethodName();
        if (initMethodName != null && !initMethodName.isEmpty() && 
                !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            try {
                // Execute initialization method via reflection
                Method initMethod = bean.getClass().getMethod(initMethodName);
                initMethod.invoke(bean);
                System.out.println("Executing custom initialization method for bean [" + beanName + "]: " + initMethodName);
            } catch (NoSuchMethodException e) {
                throw new BeansException("Could not find initialization method for bean [" + beanName + "]: " + initMethodName, e);
            }
        }
    }
    
    /**
     * Execute BeanPostProcessor pre-processing
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean instance
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }
    
    /**
     * Execute BeanPostProcessor post-processing
     * @param existingBean existing bean instance
     * @param beanName bean name
     * @return processed bean instance
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }
    
    /**
     * Register destroy method callback
     * @param beanName bean name
     * @param bean bean instance
     * @param beanDefinition bean definition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // Only singleton beans need to register destroy method
        if (!beanDefinition.isSingleton()) {
            return;
        }
        
        if (bean instanceof DisposableBean || 
                (beanDefinition.getDestroyMethodName() != null && !beanDefinition.getDestroyMethodName().isEmpty())) {
            // Create DisposableBeanAdapter and register
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition.getDestroyMethodName()));
        }
    }

    /**
     * Get early bean reference for circular dependency resolution
     * Mainly used in AOP scenarios, returns original object for normal beans, proxy object for AOP
     * 
     * @param beanName bean name
     * @param beanDefinition bean definition
     * @param bean bean instance
     * @return early reference
     */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        // Here you can perform subsequent processing on the bean, such as creating proxy objects
        // Simple implementation for now, directly return original object
        System.out.println("Getting early reference for bean [" + beanName + "]");
        return exposedObject;
    }
}