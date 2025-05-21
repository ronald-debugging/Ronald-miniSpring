package com.minispring.test.bean;

/**
 * Prototype Bean class
 * Used for testing prototype scope
 */
public class PrototypeBean {
    
    private String name;
    private long createTime;
    
    public PrototypeBean() {
        this.createTime = System.currentTimeMillis();
        System.out.println("PrototypeBean constructor, creation time: " + createTime);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    @Override
    public String toString() {
        return "PrototypeBean{" +
                "name='" + name + '\'' +
                ", createTime=" + createTime +
                '}';
    }
} 