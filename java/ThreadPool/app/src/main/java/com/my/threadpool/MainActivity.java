package com.my.threadpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ExecutorService mExecutorService;
    private BlockingQueue<Runnable> mQueue;

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mQueue = new LinkedBlockingQueue<>();
        mQueue = new SynchronousQueue<>();

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                L.d("in thread start");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                L.d("in thread end");
            }
        });

//        mExecutorService = Executors.newCachedThreadPool();
//        mExecutorService = Executors.newFixedThreadPool(3);
        mExecutorService = Executors.newSingleThreadExecutor();
//        mExecutorService = new ThreadPoolExecutor(1, 3, 5L, TimeUnit.SECONDS, mQueue);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void add() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                L.d("start");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String a = null;
                System.out.println(a.toString());
                L.d("finish");
            }
        });
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