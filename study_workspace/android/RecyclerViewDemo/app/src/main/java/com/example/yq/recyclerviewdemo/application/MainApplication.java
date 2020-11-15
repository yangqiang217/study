package com.example.yq.recyclerviewdemo.application;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {
    private static MainApplication instance;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }
}
