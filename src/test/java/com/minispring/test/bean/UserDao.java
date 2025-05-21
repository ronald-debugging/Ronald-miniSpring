package com.minispring.test.bean;

/**
 * User data access interface
 * Used for testing IoC container
 */
public interface UserDao {

    /**
     * Query user information by username
     * @param userName username
     * @return user information
     */
    String queryUserName(String userName);
} 