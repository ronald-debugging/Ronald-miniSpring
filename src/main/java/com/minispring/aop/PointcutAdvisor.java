package com.minispring.aop;

/**
 * Interface for advisors that have pointcuts
 * Extends the Advisor interface by adding a method to get the pointcut
 */
public interface PointcutAdvisor extends Advisor {
    
    /**
     * Returns the pointcut of this advisor
     * @return pointcut object
     */
    Pointcut getPointcut();
} 