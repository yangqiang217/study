package com.example.livedatademo;

public class User {
    public String name;
    public int sex;

    public User(String name, int sex) {
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
