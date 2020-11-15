package com.yq.imageviewer;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class MyApplication extends Application {

    private static Application sApplication;

    public static Context getAppContext() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sApplication = this;
    }
}
