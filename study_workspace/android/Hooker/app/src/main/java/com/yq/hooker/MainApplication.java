package com.yq.hooker;

import android.app.Application;
import android.content.Context;

/**
 * Created by yangqiang on 2018/4/9.
 */

public class MainApplication extends Application {

    private static MainApplication instance;

    public static Context getInstance() {
        return instance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }
}
