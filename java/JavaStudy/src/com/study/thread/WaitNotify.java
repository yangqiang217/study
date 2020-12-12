
package com.study.thread;

import java.util.concurrent.TimeUnit;

public class WaitNotify {

    private static boolean flag = true;
    public static Object obj = new Object();

    public static void main(String[] args) {
        Thread wait = new Thread(new Wait(), "wait");
        wait.start();
        sleep(1);
        Thread notify = new Thread(new Notify(), "notify");
        notify.start();
    }

    static class Wait implements Runnable {

        @Override
        public void run() {
            synchronized (obj) {
//                while(flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true. wait..");
                        obj.wait();
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread() + " flag is false. running..");
//                }
            }
        }
    }

    static class Notify implements Runnable {

        @Override
        public void run() {
            synchronized (obj) {
                System.out.println(Thread.currentThread() + " hold back. notify");
                obj.notifyAll();
//                flag = false;
                /**
                 * ���������Ѿ�notify�ˣ����ǲ�������ִ��wait��runing������Ϊ��Ҫ������������Ǹ�����ִ��
                 */
                sleep(3);
            }
            synchronized (obj) {
                System.out.println(Thread.currentThread() + " hold back again. sleep");
                sleep(3);
            }
            System.out.println(Thread.currentThread().getName() + " freed lock");
        }
    }
    
    static void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
