package com.minispring.aop.framework;

import com.minispring.aop.Advice;
import com.minispring.aop.MethodBeforeAdvice;
import com.minispring.aop.AfterReturningAdvice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * JDK dynamic proxy based AOP proxy implementation
 * Suitable for proxying classes that implement interfaces
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    
    // Proxy configuration
    private final AdvisedSupport advised;
    
    /**
     * Create a new JdkDynamicAopProxy
     * @param advised proxy configuration
     */
    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
    
    @Override
    public Object getProxy() {
        return getProxy(Thread.currentThread().getContextClassLoader());
    }
    
    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.getTargetSource() == null) {
            throw new IllegalStateException("TargetSource cannot be null when creating a proxy");
        }
        
        Class<?> targetClass = this.advised.getTargetSource().getTargetClass();
        // Get all interfaces implemented by the target class
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalStateException("Target class '" + targetClass.getName() + 
                    "' does not implement any interfaces, cannot create JDK proxy");
        }
        
        // Create proxy
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = null;
        
        try {
            target = this.advised.getTargetSource().getTarget();
            if (target == null) {
                throw new IllegalStateException("Target is null");
            }
            
            System.out.println("JdkDynamicAopProxy.invoke: method=" + method.getName() + ", target=" + target.getClass().getName());
            
            // Get method from target class (not from interface)
            Method targetMethod = null;
            try {
                targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                // If method not found, use interface method
                targetMethod = method;
            }
            
            // Get interceptor chain for the method
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
            System.out.println("JdkDynamicAopProxy.invoke: interceptors=" + chain.size());
            
            // If no interceptors, invoke target method directly
            if (chain.isEmpty()) {
                System.out.println("JdkDynamicAopProxy.invoke: No interceptors, direct invoke");
                return method.invoke(target, args);
            }
            
            // Create method invocation
            ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(target, targetMethod, args);
            
            // Process interceptor chain
            return processInterceptors(chain, invocation);
        } finally {
            if (target != null) {
                this.advised.getTargetSource().releaseTarget(target);
            }
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
        System.out.println("Starting to process interceptor chain with " + chain.size() + " interceptors");
        
        // Create method invocation object containing interceptor chain
        AopMethodInvocation methodInvocation = new AopMethodInvocation(invocation, chain);
        
        // Execute method invocation, which will execute all interceptors and target method in sequence
        return methodInvocation.proceed();
    }
    
    /**
     * AOP method invocation implementation class, containing interceptor chain processing logic
     */
    private static class AopMethodInvocation extends ReflectiveMethodInvocation {
        private final List<Object> interceptorsAndAdvices;
        private int currentInterceptorIndex = -1;
        
        public AopMethodInvocation(ReflectiveMethodInvocation invocation, List<Object> interceptorsAndAdvices) {
            super(invocation.getThis(), invocation.getMethod(), invocation.getArguments());
            this.interceptorsAndAdvices = interceptorsAndAdvices;
        }
        
        @Override
        public Object proceed() throws Throwable {
            // All interceptors executed, invoke target method
            if (this.currentInterceptorIndex == this.interceptorsAndAdvices.size() - 1) {
                System.out.println("Invoking target method directly");
                return super.proceed();
            }
            
            // Get next interceptor
            Object interceptorOrAdvice = this.interceptorsAndAdvices.get(++this.currentInterceptorIndex);
            System.out.println("Processing interceptor: " + interceptorOrAdvice.getClass().getName());
            
            // Handle different types of advice
            if (interceptorOrAdvice instanceof MethodBeforeAdvice) {
                System.out.println("Executing before advice");
                MethodBeforeAdvice beforeAdvice = (MethodBeforeAdvice) interceptorOrAdvice;
                beforeAdvice.before(getMethod(), getArguments(), getThis());
                return proceed();
            } else if (interceptorOrAdvice instanceof AfterReturningAdvice) {
                System.out.println("Executing after returning advice");
                Object returnValue = proceed();
                AfterReturningAdvice afterAdvice = (AfterReturningAdvice) interceptorOrAdvice;
                afterAdvice.afterReturning(returnValue, getMethod(), getArguments(), getThis());
                return returnValue;
            } else {
                throw new IllegalStateException("Unknown advice type: " + interceptorOrAdvice.getClass());
            }
        }
    }
} 