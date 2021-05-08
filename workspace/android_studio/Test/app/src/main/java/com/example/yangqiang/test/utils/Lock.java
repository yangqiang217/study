package com.example.yangqiang.test.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Lock {

    public void classLock() {
        System.out.println("classLock in " + Thread.currentThread().getName());
        synchronized (Lock.class) {
            System.out.println("classLock start " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("classLock end " + Thread.currentThread().getName());
        }
    }

    public void objLock() {
        System.out.println("objLock in " + Thread.currentThread().getName());
        synchronized (this) {
            System.out.println("objLock start " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("objLock end " + Thread.currentThread().getName());
        }
    }
}
