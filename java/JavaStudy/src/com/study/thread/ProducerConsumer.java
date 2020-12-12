package com.study.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProducerConsumer {

    private BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
    
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        Producer p = pc.new Producer();
        Consumer c = pc.new Consumer();
        
        c.start();
        p.start();
    }
    
    class Producer extends Thread {
        
        @Override
        public void run() {
            for (;;) {
                synchronized (queue) {
                    if (queue.size() == 5) {
                        try {
                            System.out.println("����");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    System.out.println("����һ��Ԫ�أ�����Ԫ�أ�" + queue.size());
                    queue.notify();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer extends Thread {
        
        @Override
        public void run() {
            for (;;) {
                synchronized (queue) {
                    if (queue.size() == 0) {
                        try {
                            System.out.println("����Ϊ��");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();
                    System.out.println("ȡ��һ��Ԫ�أ�����Ԫ�أ�" + queue.size());
                    queue.notify();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

