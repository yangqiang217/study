package com.example.yangqiang.test;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.yangqiang.test.activity.MainActivity;
import com.example.yangqiang.test.activity.SecondActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyApplication extends Application {

    private static MyApplication sIns;

    public static MyApplication getInstance() {
        return sIns;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sIns = this;
        Log.e("multiprocess", "MyApplication onCreate");

    }

    public void start(){
        Intent intent = new Intent(MyApplication.this, SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("lifecycle", "app start, intent: " + intent.hashCode());
        startActivity(intent);
    }
}
