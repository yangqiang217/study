package com.yq.jaroutput;

import okio.Timeout;

import java.text.ParseException;

/**
 * Created by yangqiang on 16-11-7.
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println("main");
        System.out.println(Person.isAlive());
        System.out.println(new Person(21, "yq").getAge());

        new Run().run();
    }
}
