package com.my.threadpool;

import android.util.Log;

public class L {
    public static void d(Object o){
        Log.d("yqpool", o + ", thread: " + Thread.currentThread().getName());
    }
}
