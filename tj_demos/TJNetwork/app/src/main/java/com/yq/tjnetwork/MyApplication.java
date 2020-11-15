package com.yq.tjnetwork;

import android.app.Application;

/**
 * Created by YangQiang on 2016/3/21.
 */
public class MyApplication extends Application {

    public static String mClientVersion = "Anroid_1.0.0";

    private static MyApplication instance;

    /*
     * 创建唯一实例
     */
    public static MyApplication getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "CPApplication has not initialed but how could you call me!!");
        }
        return instance;
    }
}
