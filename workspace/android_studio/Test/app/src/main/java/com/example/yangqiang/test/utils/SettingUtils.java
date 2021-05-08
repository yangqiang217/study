package com.example.yangqiang.test.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yangqiang.test.MyApplication;

/**
 * Created by chengyuwang on 2016/9/26.
 */
public class SettingUtils {

    public final static String NAME_SYSTEM = "system";

    private static void updateSystemSettingBoolean(String key, boolean value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);

        editor.commit();
    }

    private static void updateSystemSettingInt(String key, int value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);

        editor.commit();
    }

    private static void updateSystemSettingLong(String key, long value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);

        editor.commit();
    }

    private static int getSystemSettingInt(String key, int defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getInt(key, defaultValue);
    }

    private static boolean getSystemSettingBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getBoolean(key, defaultValue);
    }

    private static long getSystemSettingLong(String key, long defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getLong(key, defaultValue);
    }

    private static void updateSystemSettingString(String key, String value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);

        editor.commit();
    }

    private static String getSystemSettingString(String key, String defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
                NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getString(key, defaultValue);
    }
}
