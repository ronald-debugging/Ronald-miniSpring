package com.minispring.aop;

/**
 * Advisor interface, represents an aspect visitor
 * It is a combination of Pointcut and Advice, used to define where and how to apply advice
 */
public interface Advisor {
    
    /**
     * Returns the advice used by this aspect
     * @return advice object
     */
    Advice getAdvice();
    
    /**
     * Returns whether this aspect is already instantiated
     * @return true if the aspect is already instantiated
     */
    boolean isPerInstance();
} 