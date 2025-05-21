package com.minispring.test.bean;

/**
 * Test user controller class
 */
public class TestUserController {
    private TestUserService userService;
    
    public TestUserService getUserService() {
        return userService;
    }
    
    public void setUserService(TestUserService userService) {
        this.userService = userService;
    }
} 