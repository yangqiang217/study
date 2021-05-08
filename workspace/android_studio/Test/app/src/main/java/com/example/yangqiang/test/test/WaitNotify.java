package com.example.yangqiang.test.test;

import java.util.concurrent.TimeUnit;

public class WaitNotify {

    private volatile boolean isWait = false;
    private Object mObject = new Object();

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mObject) {
                    while (true) {
                        System.out.println("in run");
                        if (isWait) {
                            try {
                                System.out.println("in run wait");
                                mObject.wait();
                                System.out.println("in run, end wait");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void waitt() {
        isWait = true;
    }

    public void notifyy() {
        synchronized (mObject) {
            isWait = false;
            mObject.notify();
        }
    }
}
