package com.minispring.aop.framework;

import com.minispring.aop.Advisor;
import com.minispring.aop.PointcutAdvisor;
import com.minispring.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AOP proxy configuration support class
 * Holds configuration for target object, advice, and advisors
 */
public class AdvisedSupport {
    
    // Configuration freeze flag, if true, configuration cannot be modified
    private boolean frozen = false;
    
    // Target object source
    private TargetSource targetSource;
    
    // Target class, used when set directly
    private Class<?> targetClass;
    
    // List of advisors
    private List<Advisor> advisors = new ArrayList<>();
    
    // Method cache to avoid recalculating method interceptors
    private transient Map<Method, List<Object>> methodCache = new ConcurrentHashMap<>(32);
    
    /**
     * Set target source
     * @param targetSource target source
     */
    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }
    
    /**
     * Get target source
     * @return target source
     */
    public TargetSource getTargetSource() {
        return this.targetSource;
    }
    
    /**
     * Set target class
     * Can be set directly when not using TargetSource
     * @param targetClass target class
     */
    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
    
    /**
     * Set freeze flag
     * @param frozen if true, configuration cannot be modified
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
    
    /**
     * Check if configuration is frozen
     * @return true if configuration is frozen
     */
    public boolean isFrozen() {
        return this.frozen;
    }
    
    /**
     * Add advisor
     * @param advisor advisor to add
     */
    public void addAdvisor(Advisor advisor) {
        if (isFrozen()) {
            throw new RuntimeException("Cannot add advisor: Configuration is frozen");
        }
        this.advisors.add(advisor);
        // Clear method cache after adding new advisor
        this.methodCache.clear();
    }
    
    /**
     * Get list of advisors
     * @return list of advisors
     */
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }
    
    /**
     * Get target class
     * @return target class
     */
    public Class<?> getTargetClass() {
        if (this.targetClass != null) {
            return this.targetClass;
        }
        return this.targetSource != null ? this.targetSource.getTargetClass() : null;
    }
    
    /**
     * Check if specified method has advice
     * @param method method to check
     * @param targetClass target class
     * @return true if method has advice
     */
    public boolean hasAdvice(Method method, Class<?> targetClass) {
        return !getInterceptorsAndDynamicInterceptionAdvice(method, targetClass).isEmpty();
    }
    
    /**
     * Get interceptors and dynamic interception advice for method
     * @param method method to process
     * @param targetClass target class
     * @return list of interceptors and advice
     */
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        // Check if result is already in cache
        List<Object> cached = this.methodCache.get(method);
        if (cached != null) {
            return cached;
        }
        
        System.out.println("AdvisedSupport: Calculating interceptor chain for method " + method.getName());
        System.out.println("AdvisedSupport: Target class " + targetClass.getName());
        System.out.println("AdvisedSupport: Interfaces: " + (targetClass.getInterfaces().length > 0 ? targetClass.getInterfaces()[0].getName() : "none"));
        System.out.println("AdvisedSupport: Number of advisors " + this.advisors.size());
        
        // Calculate advice applicable to this method
        List<Object> interceptors = new ArrayList<>();
        for (Advisor advisor : this.advisors) {
            System.out.println("AdvisedSupport: Checking advisor " + advisor.getClass().getName());
            
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                System.out.println("AdvisedSupport: This is a pointcut advisor: " + pointcutAdvisor.getClass().getName());
                
                // Check if pointcut matches class
                boolean classMatches = pointcutAdvisor.getPointcut().getClassFilter().matches(targetClass);
                System.out.println("AdvisedSupport: Class match result: " + classMatches);
                
                // For classes implementing interfaces, also check if matches interface
                if (!classMatches && targetClass.getInterfaces().length > 0) {
                    for (Class<?> iface : targetClass.getInterfaces()) {
                        if (pointcutAdvisor.getPointcut().getClassFilter().matches(iface)) {
                            classMatches = true;
                            System.out.println("AdvisedSupport: Interface match result: " + classMatches + " for interface " + iface.getName());
                            break;
                        }
                    }
                }
                
                // Check if pointcut matches method
                boolean methodMatches = false;
                if (classMatches) {
                    methodMatches = pointcutAdvisor.getPointcut().getMethodMatcher().matches(method, targetClass);
                    System.out.println("AdvisedSupport: Method match result: " + methodMatches);
                    
                    // If method doesn't match, try to find and match corresponding method on interface
                    if (!methodMatches && targetClass.getInterfaces().length > 0) {
                        for (Class<?> iface : targetClass.getInterfaces()) {
                            try {
                                Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                                methodMatches = pointcutAdvisor.getPointcut().getMethodMatcher().matches(ifaceMethod, iface);
                                System.out.println("AdvisedSupport: Interface method match result: " + methodMatches + " for interface " + iface.getName());
                                if (methodMatches) {
                                    break;
                                }
                            } catch (NoSuchMethodException ex) {
                                // Method not found on interface, continue to next interface
                                System.out.println("AdvisedSupport: Interface " + iface.getName() + " does not have method " + method.getName());
                            }
                        }
                    }
                }
                
                // If matches, add advice
                if ((classMatches && methodMatches)) {
                    System.out.println("AdvisedSupport: Match successful, adding advice: " + advisor.getAdvice().getClass().getName());
                    interceptors.add(advisor.getAdvice());
                }
            } else {
                // If not a PointcutAdvisor, add advice directly
                System.out.println("AdvisedSupport: Not a pointcut advisor, adding advice directly");
                interceptors.add(advisor.getAdvice());
            }
        }
        
        System.out.println("AdvisedSupport: Final number of interceptors: " + interceptors.size());
        
        // Cache result
        this.methodCache.put(method, interceptors);
        return interceptors;
    }
}