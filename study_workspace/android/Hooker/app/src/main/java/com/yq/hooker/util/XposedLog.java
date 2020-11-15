package com.yq.hooker.util;

import android.util.Log;

/**
 * Created by yangqiang on 2018/4/8.
 */

public class XposedLog {

    private static final String TAG = "hooker_log_tag";

    public static void logd(String s) {
        Log.d(TAG, s);
    }

    public static void loge(String s) {
        Log.e(TAG, s);
    }
}
