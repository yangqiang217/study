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
                            System.out.println("满了");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    System.out.println("插入一个元素，已有元素：" + queue.size());
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
                            System.out.println("队列为空");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();
                    System.out.println("取出一个元素，已有元素：" + queue.size());
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

