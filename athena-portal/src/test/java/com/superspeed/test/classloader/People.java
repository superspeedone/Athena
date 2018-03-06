package com.superspeed.test.classloader;

public class People {

    private String name;

    public People() {
    }

    public People(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "I am a people, my name is " + this.name;
    }
}