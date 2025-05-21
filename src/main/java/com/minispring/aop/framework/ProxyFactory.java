package com.minispring.aop.framework;

import com.minispring.aop.Advisor;
import com.minispring.aop.PointcutAdvisor;

/**
 * AOP Proxy Factory
 * Creates proxy objects, automatically choosing between JDK dynamic proxy or CGLIB proxy
 */
public class ProxyFactory extends AdvisedSupport {
    
    /**
     * Create a new ProxyFactory
     */
    public ProxyFactory() {
    }
    
    /**
     * Create a new ProxyFactory with the given target object
     * @param target target object
     */
    public ProxyFactory(Object target) {
        setTargetSource(new SingletonTargetSource(target));
    }
    
    /**
     * Get proxy object
     * @return proxy object
     */
    public Object getProxy() {
        return createAopProxy().getProxy();
    }
    
    /**
     * Get proxy object using the given class loader
     * @param classLoader class loader
     * @return proxy object
     */
    public Object getProxy(ClassLoader classLoader) {
        return createAopProxy().getProxy(classLoader);
    }
    
    /**
     * Create AOP proxy
     * Choose between JDK dynamic proxy or CGLIB proxy based on whether the target class implements interfaces
     * @return AOP proxy
     */
    protected AopProxy createAopProxy() {
        // Use JDK dynamic proxy if target class implements interfaces
        if (getTargetClass().getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(this);
        }
        // Otherwise use CGLIB proxy
        return new CglibAopProxy(this);
    }
    
    /**
     * Add pointcut advisor
     * @param advisor advisor
     */
    public void addAdvisor(Advisor advisor) {
        super.addAdvisor(advisor);
    }
    
    /**
     * Check if target class can be proxied
     * @param targetClass target class
     * @return true if target class can be proxied
     */
    private boolean isProxyTargetClass(Class<?> targetClass) {
        if (targetClass == null) {
            return false;
        }
        
        // Check if there are advisors matching the target class
        for (Advisor advisor : getAdvisors()) {
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if (pointcutAdvisor.getPointcut().getClassFilter().matches(targetClass)) {
                    return true;
                }
            }
        }
        
        return false;
    }
} 