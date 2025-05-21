package com.minispring.test.bean;

/**
 * User service class
 * Used for testing IoC container
 */
public class UserService {

    private String name;
    private UserDao userDao;

    public void init() {
        System.out.println("Executing UserService initialization method");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String queryUserInfo() {
        return userDao.queryUserName(name);
    }
} 