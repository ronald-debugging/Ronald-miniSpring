package com.minispring.aop;

/**
 * Pointcut implementation that matches all classes and methods
 */
final class TruePointcut implements Pointcut {
    
    /**
     * Singleton instance
     */
    public static final TruePointcut INSTANCE = new TruePointcut();
    
    /**
     * Private constructor to prevent external instantiation
     */
    private TruePointcut() {
    }
    
    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }
    
    @Override
    public MethodMatcher getMethodMatcher() {
        return MethodMatcher.TRUE;
    }
    
    /**
     * Returns the singleton instance
     */
    private Object readResolve() {
        return INSTANCE;
    }
} 