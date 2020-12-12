
package com.study.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<String, HashMap<String, Person>> map = new ConcurrentHashMap<>();
        // Map<String, HashMap<String, Person>> map = Collections.synchronizedMap(new ConcurrentHashMap<>());

        Thread t1 = new Thread() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    HashMap<String, Person> m = new HashMap<>();
                    for (int j = 0; j < 100000; j++) {
                        Person p = new Person(j);
                        m.put(String.valueOf(j), p);
                    }
                    map.put(String.valueOf(i), m);
                }
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                while (true) {
                    if (map.get("0") != null)
                        for (Entry<String, Person> e : map.get("0").entrySet()) {
                            System.out.println(e.getValue());
                        }
                    // for (Entry<String, HashMap<String, Person>> entry: map.entrySet()) {
                    // for (Entry<String, Person> e: entry.getValue().entrySet()) {
                    // System.out.println(e.getValue());
                    // }
                    // }
                }

            }
        };

        t1.start();
        t2.start();

    }
}

class Person {
    public Person(int age) {
        this.age = age;
    }

    public void setAge(int a) {
        this.age = a;
    }

    private int age;
}
