package com.study.thread;

import java.util.concurrent.TimeUnit;

/**
 * ��ϵķ�ʽ������
 * 1. flag
 * 2. interrupt�����������run��û�ж�isInterrupted(),�ǲ��������õ�
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
         * ֱ��interrupt�������run����while��true��û���ж�isIntertupted()���Ǵ�ϲ��˵ģ������ж�isInt..
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