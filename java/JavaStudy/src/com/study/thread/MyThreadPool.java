package com.study.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class MyThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    //线程池最大限制
    private static final int MAX_WORKER_NUMBERS = 10;
    //线程池默认数量
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    //线程池最小数量
    private static final int MIN_WORKER_NUMBERS = 1;
    //工作列表。里边是外边传过来的任务
    private final LinkedList<Job> jobs = new LinkedList<>();
    //工作者列表。里边是真正在线程池运行的线程
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    //工作者线程数量
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    //线程编号生成
    private AtomicLong threadNm = new AtomicLong();
    
    public MyThreadPool() {
        initializedWorkers(DEFAULT_WORKER_NUMBERS);
    }
    
    public MyThreadPool(int num) {
        workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initializedWorkers(workerNum);
    }
    
    private void initializedWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "threadpool-worker-" + threadNm.incrementAndGet());
            thread.start();
        }
    }
    
    @Override
    public void execute(Job job) {
        if (job != null) {
            //添加一个工作，然后通知
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker: workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs) {
            //限制新增的Worker数量不能超过最大值
            if (num + this.workerNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializedWorkers(num);
            this.workerNum += num;
        }
    }

    @Override
    public void removeWorkers(int num) {
        synchronized (jobs) {
            if (num >= this.workerNum) {
                throw new IllegalArgumentException("beyond worknum");
            }
            //按照给定的数量停止worker
            int count = 0;
            while(count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    class Worker implements Runnable {

        //是否工作
        private volatile boolean running = true;
        
        @Override
        public void run() {
            while(running) {
                Job job = null;
                synchronized (jobs) {
                    //如果工作列表是空的，wait
                    while(jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知到外部对workerThread的中断，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    //取出一个job
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                    }
                }
            }
        }
        
        public void shutdown() {
            running = false;
        }
    }
}


interface ThreadPool<Job extends Runnable> {
    void execute(Job job);
    void shutdown();
    void addWorkers(int num);
    void removeWorkers(int num);
    int getJobSize();
}