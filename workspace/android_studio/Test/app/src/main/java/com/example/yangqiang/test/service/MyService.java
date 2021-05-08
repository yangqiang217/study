package com.example.yangqiang.test.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private MyBinder mMyBinder = new MyBinder();
    private volatile boolean stop = false;

    public MyService() {
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public int get() {
        return 100;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lifecycle", "MyService onCreate");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    try {
                        Log.d("lifecycle", "tik tok");
                        TimeUnit.MILLISECONDS.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("lifecycle", "MyService onRebind");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("lifecycle", "MyService onConfigurationChanged");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lifecycle", "MyService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("lifecycle", "MyService onBind, " + Thread.currentThread().getName());
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("lifecycle", "MyService onUnbind");
        stop = true;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "MyService onDestroy");
    }
}