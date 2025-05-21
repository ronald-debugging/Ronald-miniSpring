package com.minispring.test.service;

import com.minispring.test.bean.TestBean;

/**
 * Test service class
 */
public class TestService {
    
    private TestBean testBean;
    private String message;
    
    public TestService() {
    }
    
    public TestBean getTestBean() {
        return testBean;
    }
    
    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getServiceInfo() {
        return message + " - " + testBean.toString();
    }
    
    @Override
    public String toString() {
        return "TestService{" +
                "testBean=" + testBean +
                ", message='" + message + '\'' +
                '}';
    }
} 