package com.minispring.test;

import com.minispring.aop.MethodBeforeAdvice;
import com.minispring.aop.aspectj.AspectJExpressionPointcut;
import com.minispring.aop.framework.ProxyFactory;
import com.minispring.aop.support.DefaultPointcutAdvisor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * AOP functionality test class
 */
public class AopTest {
    
    /**
     * Test basic AOP functionality
     */
    @Test
    public void testAopProxy() throws Exception {
        // 1. Create target object
        TestService target = new TestService();
        System.out.println("Target class: " + target.getClass().getName());
        
        // 2. Create pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.minispring.test.AopTest$ITestService.*(..))");
        System.out.println("Pointcut matches TestService: " + pointcut.matches(TestService.class));
        System.out.println("Pointcut matches sayHello method: " + pointcut.matches(TestService.class.getMethod("sayHello"), TestService.class));
        System.out.println("Pointcut matches ITestService sayHello method: " + pointcut.matches(ITestService.class.getMethod("sayHello"), ITestService.class));
        
        // 3. Create advice
        TestBeforeAdvice beforeAdvice = new TestBeforeAdvice();
        
        // 4. Create Advisor (combination of pointcut and advice)
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
        
        // 5. Create proxy using proxy factory
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor);
        ITestService proxy = (ITestService) proxyFactory.getProxy();
        
        // 6. Call proxy method, should trigger advice
        Assertions.assertEquals("TestService.sayHello()", proxy.sayHello());
        Assertions.assertEquals(1, beforeAdvice.getCounter());
        
        // 7. Multiple calls, counter should increase
        proxy.sayHello();
        Assertions.assertEquals(2, beforeAdvice.getCounter());
    }
    
    /**
     * Test CGLIB proxy
     */
    @Test
    public void testCglibProxy() throws Exception {
        // 1. Create target object (class without interface)
        NonInterfaceService target = new NonInterfaceService();
        
        // 2. Create pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.minispring.test.AopTest$NonInterfaceService.*(..))");
        
        // 3. Create advice
        TestBeforeAdvice beforeAdvice = new TestBeforeAdvice();
        
        // 4. Create Advisor (combination of pointcut and advice)
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
        
        // 5. Create proxy using proxy factory
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor);
        NonInterfaceService proxy = (NonInterfaceService) proxyFactory.getProxy();
        
        // 6. Call proxy method, should trigger advice
        Assertions.assertEquals("NonInterfaceService.doSomething()", proxy.doSomething());
        Assertions.assertEquals(1, beforeAdvice.getCounter());
    }
    
    /**
     * Service interface for testing
     */
    public interface ITestService {
        String sayHello();
    }
    
    /**
     * Service implementation for testing
     */
    static class TestService implements ITestService {
        @Override
        public String sayHello() {
            return "TestService.sayHello()";
        }
    }
    
    /**
     * Service class without interface, used for testing CGLIB proxy
     */
    static class NonInterfaceService {
        public String doSomething() {
            return "NonInterfaceService.doSomething()";
        }
    }
    
    /**
     * Test before advice
     */
    static class TestBeforeAdvice implements MethodBeforeAdvice {
        private int counter = 0;
        
        @Override
        public void before(Method method, Object[] args, Object target) throws Throwable {
            counter++;
            System.out.println("TestBeforeAdvice.before called, counter = " + counter);
            System.out.println("Before method [" + method.getName() + "] on target [" + target.getClass().getName() + "]");
        }
        
        public int getCounter() {
            return counter;
        }
    }
} 