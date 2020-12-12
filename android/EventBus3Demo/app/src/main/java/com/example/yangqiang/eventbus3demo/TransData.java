package com.example.yangqiang.eventbus3demo;

/**
 * Created by yangqiang on 05/12/2017.
 */

public class TransData {

    public TransData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name;
    public int age;


    @Override
    public String toString() {
        return "name: " + name + ", age: " + age;
    }
}
