package com.minispring.aop.support;

import com.minispring.aop.Advice;
import com.minispring.aop.Pointcut;
import com.minispring.aop.PointcutAdvisor;

/**
 * Default implementation of PointcutAdvisor
 * Can be used with any Pointcut and Advice
 */
public class DefaultPointcutAdvisor implements PointcutAdvisor {
    
    private Advice advice;
    private Pointcut pointcut = Pointcut.TRUE;
    
    /**
     * Create an empty DefaultPointcutAdvisor
     * Advice must be set through setAdvice method
     */
    public DefaultPointcutAdvisor() {
    }
    
    /**
     * Create a DefaultPointcutAdvisor with the given advice
     * Will use the default Pointcut.TRUE pointcut
     * @param advice advice to use
     */
    public DefaultPointcutAdvisor(Advice advice) {
        this.advice = advice;
    }
    
    /**
     * Create a DefaultPointcutAdvisor with the given pointcut and advice
     * @param pointcut pointcut to use
     * @param advice advice to use
     */
    public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice) {
        this.pointcut = pointcut;
        this.advice = advice;
    }
    
    /**
     * Set the advice for this aspect
     * @param advice advice to set
     */
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
    
    /**
     * Set the pointcut for this aspect
     * @param pointcut pointcut to set
     */
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = (pointcut != null ? pointcut : Pointcut.TRUE);
    }
    
    @Override
    public Advice getAdvice() {
        return this.advice;
    }
    
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
    
    @Override
    public boolean isPerInstance() {
        return true;
    }
} 