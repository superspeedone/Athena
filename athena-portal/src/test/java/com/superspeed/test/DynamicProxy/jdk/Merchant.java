package com.superspeed.test.DynamicProxy.jdk;

/**
 * 代理商
 */
public class Merchant implements Person {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Merchant(String name) {
        this.name = name;
    }

    @Override
    public void sale() {
        System.out.println("代理商代理工程买东西");

    }

}
