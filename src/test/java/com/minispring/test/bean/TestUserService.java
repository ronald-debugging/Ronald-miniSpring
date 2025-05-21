package com.minispring.test.bean;

/**
 * Test user service class (property injection)
 */
public class TestUserService {
    private String name;
    private TestUserDao userDao;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TestUserDao getUserDao() {
        return userDao;
    }
    
    public void setUserDao(TestUserDao userDao) {
        this.userDao = userDao;
    }
} 