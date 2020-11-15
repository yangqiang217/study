package com.study.thread;

import java.util.concurrent.TimeUnit;

/**
 * 打断的方式有两种
 * 1. flag
 * 2. interrupt，这种如果在run里没判断isInterrupted(),是不会起作用的
 * @author YangQiang
 *
 */
public class Interrupt {

    public static void main(String[] args) {
        MyThread mth = new MyThread();
        mth.start();
        
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 直接interrupt，而如果run里是while（true）没有判断isIntertupted()，是打断不了的，除非判断isInt..
         */
        mth.interrupt();
        mth.flag = false;
    }

    private static class MyThread extends Thread {
        
        public volatile boolean flag = true;
        
        @Override
        public void run() {
            while(/*!this.isInterrupted()*/flag) {
                System.out.println("running..");
            }
        }
    }
}