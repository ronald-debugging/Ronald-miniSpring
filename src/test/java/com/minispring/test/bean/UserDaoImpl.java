package com.minispring.test.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * User data access implementation class
 * Used for testing IoC container
 */
public class UserDaoImpl implements UserDao {

    private static final Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("Zhang San", "Beijing");
        userMap.put("Li Si", "Shanghai");
        userMap.put("Wang Wu", "Guangzhou");
    }

    @Override
    public String queryUserName(String userName) {
        return userMap.get(userName);
    }
} 