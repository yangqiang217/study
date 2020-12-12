package com.yq.rxjava;

import android.util.Log;

public class L {

    public static void print(Object content) {
        Log.d("com/yq/rxjava/diy", content + ", thread: " + Thread.currentThread().getName());
    }
}
