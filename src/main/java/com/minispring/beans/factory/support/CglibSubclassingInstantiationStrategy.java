package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * CGLIB Subclassing Instantiation Strategy
 * Uses CGLIB to dynamically generate subclasses for bean instantiation, suitable for classes without default constructors
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        try {
            // Create CGLIB enhancer
            Enhancer enhancer = new Enhancer();
            // Set the class for which to create a subclass
            enhancer.setSuperclass(beanDefinition.getBeanClass());
            // Set callback function, using NoOp.INSTANCE here, meaning no interception logic
            // Note: If AOP or other enhancement logic is needed, use MethodInterceptor instead
            enhancer.setCallback(NoOp.INSTANCE);

            // Parameter validation and processing
            if (ctor == null) {
                // If no constructor specified, use default constructor
                return enhancer.create();
            }
            
            // Convert null args to empty array to avoid NPE
            if (args == null) {
                args = new Object[0];
            }
            
            // Check if parameter count matches
            if (args.length != ctor.getParameterCount()) {
                throw new BeansException("Constructor parameter count mismatch: " + beanName + 
                        ", expected " + ctor.getParameterCount() + " parameters but got " + args.length + " parameters");
            }
            
            // Instantiate using specified constructor
            return enhancer.create(ctor.getParameterTypes(), args);
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate bean using CGLIB [" + beanName + "]", e);
        }
    }
} 