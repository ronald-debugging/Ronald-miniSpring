package com.minispring.test.bean;

/**
 * Test user service class (constructor injection)
 */
public class TestUserServiceWithConstructor {
    private final TestUserDao userDao;
    
    public TestUserServiceWithConstructor() {
        this.userDao = null;
    }
    
    public TestUserServiceWithConstructor(TestUserDao userDao) {
        this.userDao = userDao;
    }
    
    public TestUserDao getUserDao() {
        return userDao;
    }
} 