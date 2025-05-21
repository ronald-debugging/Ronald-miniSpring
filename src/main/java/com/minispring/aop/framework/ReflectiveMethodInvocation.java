package com.minispring.aop.framework;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * Reflective implementation of method invocation
 * This class implements the MethodInvocation interface for invoking target methods through reflection
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    // Target object
    protected final Object target;
    // Target method
    protected final Method method;
    // Method arguments
    protected final Object[] arguments;

    /**
     * Constructor
     * @param target target object
     * @param method target method
     * @param arguments method arguments
     */
    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Get the method object
     * @return method object
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * Get method arguments
     * @return array of method arguments
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Get target object
     * @return target object
     */
    @Override
    public Object getThis() {
        return target;
    }

    /**
     * Execute method invocation
     * Invoke target method through reflection mechanism
     * @return method execution result
     * @throws Throwable if an exception occurs during method invocation
     */
    @Override
    public Object proceed() throws Throwable {
        return invokeJoinPoint();
    }

    /**
     * Invoke join point
     * This method performs the actual reflection invocation
     * @return method execution result
     * @throws Throwable if an exception occurs during method invocation
     */
    protected Object invokeJoinPoint() throws Throwable {
        // Ensure method is accessible
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        // Invoke target method through reflection
        return method.invoke(target, arguments);
    }
} 