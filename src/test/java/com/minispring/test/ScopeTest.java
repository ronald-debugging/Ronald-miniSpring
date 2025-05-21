package com.minispring.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.Scope;
import com.minispring.beans.factory.config.SingletonScope;
import com.minispring.beans.factory.config.PrototypeScope;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.BeansException;

/**
 * 作用域测试类
 */
public class ScopeTest {
    
    /**
     * 测试单例作用域
     */
    @Test
    public void testSingletonScope() {
        Scope singletonScope = new SingletonScope();
        
        // 第一次获取Bean
        Object bean1 = singletonScope.get("testBean", new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return new TestBean();
            }
        });
        
        // 第二次获取Bean
        Object bean2 = singletonScope.get("testBean", new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return new TestBean();
            }
        });
        
        // 在单例作用域中，两次获取的应该是同一个对象
        assertSame(bean1, bean2, "单例作用域应返回同一个对象实例");
    }
    
    /**
     * 测试原型作用域
     */
    @Test
    public void testPrototypeScope() {
        Scope prototypeScope = new PrototypeScope();
        
        // 第一次获取Bean
        Object bean1 = prototypeScope.get("testBean", new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return new TestBean();
            }
        });
        
        // 第二次获取Bean
        Object bean2 = prototypeScope.get("testBean", new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return new TestBean();
            }
        });
        
        // 在原型作用域中，两次获取的应该是不同的对象
        assertNotSame(bean1, bean2, "原型作用域应返回不同的对象实例");
    }
    
    /**
     * 测试销毁回调
     */
    @Test
    public void testDestructionCallback() {
        // 实现销毁回调测试
        SingletonScope singletonScope = new SingletonScope();
        
        // 获取Bean并注册销毁回调
        TestBean bean = (TestBean) singletonScope.get("testBean", new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return new TestBean();
            }
        });
        
        // 注册销毁回调
        singletonScope.registerDestructionCallback("testBean", new Runnable() {
            @Override
            public void run() {
                bean.destroy();
            }
        });
        
        // 执行销毁
        assertFalse(bean.isDestroyed(), "销毁前Bean不应该是已销毁状态");
        singletonScope.destroySingletons();
        assertTrue(bean.isDestroyed(), "销毁后Bean应该是已销毁状态");
    }
    
    /**
     * 测试用的Bean类
     */
    static class TestBean {
        private boolean destroyed = false;
        
        public void destroy() {
            this.destroyed = true;
        }
        
        public boolean isDestroyed() {
            return destroyed;
        }
    }
} 