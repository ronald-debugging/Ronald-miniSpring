package com.minispring.aop;

/**
 * Pointcut interface, used to define interception rules.
 * A pointcut is a matching rule used to determine which methods should be intercepted.
 * It combines class matcher and method matcher to define precise interception scope.
 */
public interface Pointcut {
    
    /**
     * Returns the class matcher of the pointcut, used to determine which classes should be intercepted
     * @return class matcher
     */
    ClassFilter getClassFilter();
    
    /**
     * Returns the method matcher of the pointcut, used to determine which methods in a class should be intercepted
     * @return method matcher
     */
    MethodMatcher getMethodMatcher();
    
    /**
     * Constant representing a pointcut that matches everything
     */
    Pointcut TRUE = TruePointcut.INSTANCE;
} 