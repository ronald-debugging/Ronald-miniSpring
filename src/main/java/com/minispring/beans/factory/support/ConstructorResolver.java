package com.minispring.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

import com.minispring.beans.BeansException;
import com.minispring.beans.TypeConverter;
import com.minispring.beans.SimpleTypeConverter;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.config.DependencyDescriptor;
import com.minispring.core.DefaultParameterNameDiscoverer;
import com.minispring.core.ParameterNameDiscoverer;

/**
 * Constructor Resolver
 * Used to resolve constructor arguments and perform autowiring
 */
public class ConstructorResolver {

    private final AbstractAutowireCapableBeanFactory beanFactory;
    private final TypeConverter typeConverter;
    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final Set<String> inCreationBeans;

    /**
     * Create a constructor resolver
     * 
     * @param beanFactory bean factory
     */
    public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.typeConverter = new SimpleTypeConverter();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        this.inCreationBeans = new HashSet<>();
    }

    /**
     * Check for circular dependency
     * 
     * @param beanName bean name
     * @param dependencyType dependency type
     * @return whether circular dependency exists
     */
    private boolean isCircularDependency(String beanName, Class<?> dependencyType) {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        // Check if any bean of the dependency type is currently in creation
        String[] dependencyNames = factory.getBeanNamesForType(dependencyType);
        
        for (String dependencyName : dependencyNames) {
            if (beanFactory.isSingletonCurrentlyInCreation(dependencyName)) {
                System.out.println("Detected circular dependency: " + beanName + " -> " + dependencyName + "(" + dependencyType.getName() + ")");
                return true;
            }
        }
        return false;
    }

    /**
     * Get early bean reference
     * 
     * @param beanName bean name
     * @return early bean reference
     */
    private Object getEarlyBeanReference(String beanName) {
        // Try to get early reference from cache
        Object earlyReference = beanFactory.getSingleton(beanName);
        if (earlyReference != null) {
            System.out.println("Got early reference for bean [" + beanName + "]");
            return earlyReference;
        }
        System.out.println("Could not get early reference for bean [" + beanName + "], might not be in creation process yet");
        return null;
    }

    /**
     * Autowire constructor
     * 
     * @param beanName bean name
     * @param beanDefinition bean definition
     * @param constructors candidate constructors
     * @param args explicitly provided arguments
     * @return resolved constructor and arguments
     * @throws BeansException if constructor cannot be resolved
     */
    public BeanInstantiationContext autowireConstructor(String beanName, BeanDefinition beanDefinition, 
            Constructor<?>[] constructors, Object[] args) throws BeansException {
        
        System.out.println("Starting to resolve constructor: " + beanName + ", constructor count: " + (constructors != null ? constructors.length : 0));
        
        // If no constructors provided, use default constructor
        if (constructors == null || constructors.length == 0) {
            try {
                Constructor<?> defaultCtor = beanDefinition.getBeanClass().getDeclaredConstructor();
                System.out.println("Using default constructor: " + defaultCtor);
                return new BeanInstantiationContext(defaultCtor, new Object[0]);
            } catch (NoSuchMethodException e) {
                throw new BeansException("Could not find default constructor: " + beanDefinition.getBeanClass().getName(), e);
            }
        }
        
        // If arguments provided, match constructor by argument types
        if (args != null && args.length > 0) {
            System.out.println("Arguments provided, trying to match constructor: " + Arrays.toString(args));
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == args.length) {
                    Class<?>[] paramTypes = constructor.getParameterTypes();
                    boolean match = true;
                    Object[] convertedArgs = new Object[args.length];
                    
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] != null) {
                            // Try type conversion
                            if (!paramTypes[i].isInstance(args[i])) {
                                try {
                                    convertedArgs[i] = typeConverter.convert(args[i], paramTypes[i]);
                                } catch (Exception e) {
                                    match = false;
                                    break;
                                }
                            } else {
                                convertedArgs[i] = args[i];
                            }
                        }
                    }
                    
                    if (match) {
                        System.out.println("Found matching constructor: " + constructor);
                        return new BeanInstantiationContext(constructor, convertedArgs);
                    }
                }
            }
        }
        
        // Sort constructors by parameter count (prefer constructors with more parameters)
        Arrays.sort(constructors, (c1, c2) -> c2.getParameterCount() - c1.getParameterCount());
        
        // Try to find constructor that can be autowired
        for (Constructor<?> constructor : constructors) {
            System.out.println("Trying to autowire constructor: " + constructor);
            
            try {
                // Get parameter names
                String[] paramNames = parameterNameDiscoverer.getParameterNames(constructor);
                Parameter[] parameters = constructor.getParameters();
                Object[] resolvedArgs = new Object[parameters.length];
                
                for (int i = 0; i < parameters.length; i++) {
                    String paramName = paramNames != null ? paramNames[i] : parameters[i].getName();
                    Class<?> paramType = parameters[i].getType();
                    
                    // Check for circular dependency
                    if (isCircularDependency(beanName, paramType)) {
                        resolvedArgs[i] = getEarlyBeanReference(beanName);
                        continue;
                    }
                    
                    // Create dependency descriptor
                    DependencyDescriptor descriptor = new DependencyDescriptor(parameters[i], true);
                    descriptor.setParameterName(paramName);
                    
                    try {
                        resolvedArgs[i] = resolveDependent(descriptor, beanName);
                    } catch (BeansException e) {
                        System.out.println("Could not resolve parameter: " + paramName + ", type: " + paramType.getName());
                        throw e;
                    }
                }
                
                System.out.println("Successfully resolved constructor arguments: " + Arrays.toString(resolvedArgs));
                return new BeanInstantiationContext(constructor, resolvedArgs);
                
            } catch (BeansException e) {
                System.out.println("Constructor autowiring failed: " + e.getMessage());
                // Continue with next constructor
            }
        }
        
        // If no suitable constructor found, try default constructor
        try {
            Constructor<?> defaultCtor = beanDefinition.getBeanClass().getDeclaredConstructor();
            System.out.println("No suitable constructor found, using default constructor: " + defaultCtor);
            return new BeanInstantiationContext(defaultCtor, new Object[0]);
        } catch (NoSuchMethodException e) {
            throw new BeansException("Could not find suitable constructor: " + beanDefinition.getBeanClass().getName(), e);
        }
    }

    /**
     * Resolve constructor arguments
     * 
     * @param beanName bean name
     * @param beanDefinition bean definition
     * @param constructor constructor
     * @return resolved argument array
     * @throws BeansException if arguments cannot be resolved
     */
    public Object[] resolveConstructorArguments(String beanName, BeanDefinition beanDefinition, 
            Constructor<?> constructor) throws BeansException {
        
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];
        
        System.out.println("Starting to resolve constructor arguments: " + constructor + ", parameter count: " + parameters.length);
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            DependencyDescriptor descriptor = new DependencyDescriptor(parameter, true);
            
            System.out.println("Resolving parameter: " + parameter.getName() + ", type: " + parameter.getType().getName());
            
            try {
                args[i] = resolveDependent(descriptor, beanName);
                System.out.println("Successfully resolved parameter: " + parameter.getName() + " = " + args[i]);
            } catch (BeansException e) {
                System.out.println("Failed to resolve parameter: " + parameter.getName() + ", error: " + e.getMessage());
                throw new BeansException("Could not resolve constructor parameter: " + parameter.getName() + " type: " + 
                        parameter.getType().getName() + " for bean: " + beanName, e);
            }
        }
        
        return args;
    }

    /**
     * Resolve dependency
     * 
     * @param descriptor dependency descriptor
     * @param beanName current bean name
     * @return resolved dependency object
     * @throws BeansException if dependency cannot be resolved
     */
    private Object resolveDependent(DependencyDescriptor descriptor, String beanName) throws BeansException {
        Class<?> type = descriptor.getDependencyType();
        String dependencyName = descriptor.getDependencyName();
        
        System.out.println("Resolving dependency: type=" + type.getName() + ", name=" + dependencyName);
        
        // If dependency name specified, get directly
        if (dependencyName != null && !dependencyName.isEmpty()) {
            System.out.println("Getting bean by name: " + dependencyName);
            try {
                return beanFactory.getBean(dependencyName);
            } catch (BeansException e) {
                // If getting by name fails, try other methods
                System.out.println("Failed to get bean by name: " + e.getMessage());
            }
        }
        
        // Try to get by type
        try {
            System.out.println("Getting bean by type: " + type.getName());
            DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
            String[] beanNames = listableBeanFactory.getBeanNamesForType(type);
            
            if (beanNames.length == 1) {
                String autowiredBeanName = beanNames[0];
                System.out.println("Found unique matching bean: " + autowiredBeanName);
                return beanFactory.getBean(autowiredBeanName);
            } else if (beanNames.length > 1) {
                // If multiple matching beans found, try in the following priority:
                // 1. Use parameter name
                // 2. Use dependency name (if available)
                // 3. Use type name (first letter lowercase)
                String paramName = descriptor.getMethodParameter() != null ? 
                    descriptor.getMethodParameter().getName() : null;
                
                // 1. Try using parameter name
                if (paramName != null && Arrays.asList(beanNames).contains(paramName)) {
                    System.out.println("Found matching bean by parameter name: " + paramName);
                    return beanFactory.getBean(paramName);
                }
                
                // 2. Try using dependency name
                if (dependencyName != null && Arrays.asList(beanNames).contains(dependencyName)) {
                    System.out.println("Found matching bean by dependency name: " + dependencyName);
                    return beanFactory.getBean(dependencyName);
                }
                
                // 3. Try using type name (first letter lowercase)
                String typeNameBean = type.getSimpleName().substring(0, 1).toLowerCase() + 
                    type.getSimpleName().substring(1);
                if (Arrays.asList(beanNames).contains(typeNameBean)) {
                    System.out.println("Found matching bean by type name: " + typeNameBean);
                    return beanFactory.getBean(typeNameBean);
                }
                
                // If no matching bean found, throw exception
                throw new BeansException("Found multiple beans of type '" + type.getName() + "': " + 
                    String.join(", ", beanNames));
            }
            
            // If no matching bean found, try using type name (first letter lowercase)
            String typeNameBean = type.getSimpleName().substring(0, 1).toLowerCase() + 
                type.getSimpleName().substring(1);
            if (beanFactory.containsBean(typeNameBean)) {
                System.out.println("Found bean using type name: " + typeNameBean);
                return beanFactory.getBean(typeNameBean);
            }
            
            throw new BeansException("No bean found of type '" + type.getName() + "'");
            
        } catch (BeansException e) {
            if (descriptor.isRequired()) {
                throw e;
            }
            return null;
        }
    }

    /**
     * Bean instantiation context
     * Holds constructor and resolved arguments
     */
    public static class BeanInstantiationContext {
        private final Constructor<?> constructor;
        private final Object[] args;

        public BeanInstantiationContext(Constructor<?> constructor, Object[] args) {
            this.constructor = constructor;
            this.args = args;
        }

        public Constructor<?> getConstructor() {
            return constructor;
        }

        public Object[] getArgs() {
            return args;
        }
    }
} 