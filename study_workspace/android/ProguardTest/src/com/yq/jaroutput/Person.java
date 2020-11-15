package com.yq.jaroutput;

import java.io.Serializable;

public class Person implements Serializable {

    private int age;
    private String name;
    private static final boolean isAlive = true;

    private class InnerPerson1 {
        public InnerPerson1() {
        }
    }

    public static class InnerPerson2 {
        public InnerPerson2() {
        }
    }

    public class InnerPerson3 {
        public InnerPerson3() {
        }
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;

        new InnerPerson1();
        new InnerPerson2();
        new InnerPerson3();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name + getLastName();
    }

    private String getLastName() {//private类
        return " lastName";
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getKind() {
        return "human";
    }

    public static final boolean isAlive() {
        return isAlive;
    }
}
