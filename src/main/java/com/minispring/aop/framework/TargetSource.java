package com.minispring.aop.framework;

/**
 * TargetSource interface used to encapsulate the target object
 * The ultimate target of AOP proxy
 */
public interface TargetSource {
    
    /**
     * Returns the type of target object
     * @return target type
     */
    Class<?> getTargetClass();
    
    /**
     * Returns whether the same target object is returned
     * @return true if each call returns the same target object
     */
    boolean isStatic();
    
    /**
     * Get the target object
     * May be called for each method invocation
     * @return target object
     */
    Object getTarget() throws Exception;
    
    /**
     * Release the target object
     * May be called after method invocation is complete
     * @param target the target object to release
     */
    void releaseTarget(Object target) throws Exception;
} 