package com.my.threadpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private ExecutorService mExecutorService;
    private BlockingQueue<Runnable> mQueue;

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = new LinkedBlockingQueue<>(3);
//        mQueue = new ArrayBlockingQueue<>(3);
//        mQueue = new SynchronousQueue<>();

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                L.d("uncaughtException1, e:" + e.getMessage());
//            }
//        });
//        mThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                L.d("uncaughtException, e:" + e.getMessage());
//            }
//        });

//        mExecutorService = Executors.newCachedThreadPool();
//        mExecutorService = Executors.newFixedThreadPool(3);
//        mExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService = new ThreadPoolExecutor(2, 5, 5L, TimeUnit.SECONDS, mQueue);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = null;
                System.out.println(a.toString());
                L.d("onClick");
            }
        });
    }

    private void add() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                while (true) {
                    L.d("run " + this.hashCode());
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        L.d("InterruptedException");
                        e.printStackTrace();
                    }
//                }
            }
        };
        L.d("runnable hash: " + runnable.hashCode());
        mExecutorService.execute(runnable);

        L.d("queue size: " + mQueue.size());

//        mExecutorService.submit(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                L.d("start");
//                try {
//                    TimeUnit.SECONDS.sleep(3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                L.d("finish");
//                return "shit";
//            }
//        });
    }

    private void start(){
        mThread.start();
    }
}