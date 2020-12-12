package com.study.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        
        Future<String> future = exec.submit(new TaskWithResult());
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class TaskWithResult implements Callable<String>{
    
    @Override
    public String call() throws Exception {
        for(int i = 0; i < 5; i++){
            System.out.println("looping " + i + " of thread: " + Thread.currentThread().getName());
            Thread.sleep(1000);
        }
        return "result of TaskWithResult";
    }
    
}