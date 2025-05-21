package com.minispring.aop.framework;

/**
 * Singleton target source implementation
 * Returns the same target object for each call
 */
public class SingletonTargetSource implements TargetSource {
    
    private final Object target;
    
    /**
     * Create a new SingletonTargetSource
     * @param target target object
     */
    public SingletonTargetSource(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Target object must not be null");
        }
        this.target = target;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return target.getClass();
    }
    
    @Override
    public boolean isStatic() {
        return true;
    }
    
    @Override
    public Object getTarget() {
        return this.target;
    }
    
    @Override
    public void releaseTarget(Object target) {
        // No need to release singleton
    }
    
    /**
     * Compare if this target source equals another target source
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SingletonTargetSource)) {
            return false;
        }
        SingletonTargetSource otherTargetSource = (SingletonTargetSource) other;
        return this.target.equals(otherTargetSource.target);
    }
    
    /**
     * Returns the hash code of this target source
     */
    @Override
    public int hashCode() {
        return this.target.hashCode();
    }
} 