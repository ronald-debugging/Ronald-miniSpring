package com.minispring.aop.framework;

import com.minispring.aop.AfterReturningAdvice;
import com.minispring.aop.MethodBeforeAdvice;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * CGLIB-based AOP proxy implementation
 * Suitable for proxying classes that don't implement interfaces
 */
public class CglibAopProxy implements AopProxy {
    
    // Proxy configuration
    private final AdvisedSupport advised;
    
    /**
     * Create a new CglibAopProxy
     * @param advised proxy configuration
     */
    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
    
    @Override
    public Object getProxy() {
        return getProxy(null);
    }
    
    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.getTargetSource() == null) {
            throw new IllegalStateException("TargetSource cannot be null when creating a proxy");
        }
        
        Class<?> targetClass = this.advised.getTargetSource().getTargetClass();
        if (targetClass == null) {
            throw new IllegalStateException("Target class must be available for creating a CGLIB proxy");
        }
        
        // Create CGLIB enhancer
        Enhancer enhancer = new Enhancer();
        if (classLoader != null) {
            enhancer.setClassLoader(classLoader);
        }
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new CglibMethodInterceptor());
        
        // Create proxy instance
        return enhancer.create();
    }
    
    /**
     * CGLIB method interceptor
     * Handles proxy method invocations
     */
    private class CglibMethodInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = null;
            
            try {
                target = advised.getTargetSource().getTarget();
                if (target == null) {
                    throw new IllegalStateException("Target is null");
                }
                
                // Get interceptor chain for the method
                List<Object> chain = advised.getInterceptorsAndDynamicInterceptionAdvice(method, target.getClass());
                
                // If no interceptors, invoke target method directly
                if (chain.isEmpty()) {
                    return methodProxy.invoke(target, args);
                }
                
                // Create method invocation
                CglibMethodInvocation invocation = new CglibMethodInvocation(target, method, args, methodProxy);
                
                // Process interceptor chain
                return processInterceptors(chain, invocation);
            } finally {
                if (target != null) {
                    advised.getTargetSource().releaseTarget(target);
                }
            }
        }
    }
    
    /**
     * CGLIB method invocation
     * Extends ReflectiveMethodInvocation to use CGLIB's MethodProxy for target method invocation
     */
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
        
        private final MethodProxy methodProxy;
        
        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }
        
        @Override
        protected Object invokeJoinPoint() throws Throwable {
            return this.methodProxy.invoke(getThis(), getArguments());
        }
    }
    
    /**
     * Process interceptor chain
     * @param chain interceptor chain
     * @param invocation method invocation
     * @return invocation result
     * @throws Throwable if an error occurs during processing
     */
    private Object processInterceptors(List<Object> chain, ReflectiveMethodInvocation invocation) throws Throwable {
        Object returnValue = null;
        
        // Process before advice
        for (Object advice : chain) {
            if (advice instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advice).before(
                        invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
        }
        
        // Invoke target method
        returnValue = invocation.proceed();
        
        // Process after returning advice
        for (Object advice : chain) {
            if (advice instanceof AfterReturningAdvice) {
                ((AfterReturningAdvice) advice).afterReturning(
                        returnValue, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
        }
        
        return returnValue;
    }
} 