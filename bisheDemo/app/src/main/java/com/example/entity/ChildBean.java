package com.example.entity;

public class ChildBean {

    private String name;
    private String sign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public ChildBean(String name, String sign) {
        this.name = name;
        this.sign = sign;
    }

    public ChildBean() {
    }

}