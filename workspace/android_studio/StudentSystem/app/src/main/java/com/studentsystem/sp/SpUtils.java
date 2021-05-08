package com.studentsystem.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.studentsystem.MyApplication;
import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Student;


/**
 * yq整个类
 * 管理sharedpreference，数据存储和读取
 */
public class SpUtils {
    
    private static final String NAME_SYSTEM = "system";

    private static final String KEY_LOGIN_STU = "login_stu";
    private static final String KEY_LOGIN_ADMIN = "login_admin";

    //-------------外部调用方法统一写这，做一层封装
    //设置sp，存放登录student的json，null表示退出登录清除这个字段
    public static void setLoginedStudent(@Nullable Student student) {
        if (student == null) {
            updateSystemSettingString(KEY_LOGIN_STU, "");
        } else {
            updateSystemSettingString(KEY_LOGIN_STU, new Gson().toJson(student));
        }
    }
    public static Student getLoginedStudent() {
        String json = getSystemSettingString(KEY_LOGIN_STU, "");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, Student.class);
        } else {
            return null;
        }
    }
    //设置sp，存放登录admin的json，null表示退出登录清除这个字段
    public static void setLoginedAdmin(@Nullable Admin admin) {
        if (admin == null) {
            updateSystemSettingString(KEY_LOGIN_ADMIN, "");
        } else {
            updateSystemSettingString(KEY_LOGIN_ADMIN, new Gson().toJson(admin));
        }
    }
    public static Admin getLoginedAdmin() {
        String json = getSystemSettingString(KEY_LOGIN_ADMIN, "");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, Admin.class);
        } else {
            return null;
        }
    }

    //-------------内部方法，不暴露给外部
    /**
     * 管理sp的int值
     */
    private static int getSystemSettingInt(String key, int defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getInt(key, defaultValue);
    }
    private static void updateSystemSettingInt(String key, int value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);

        editor.apply();
    }

    /**
     * 管理sp的boolean值
     */
    private static boolean getSystemSettingBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getBoolean(key, defaultValue);
    }
    private static void updateSystemSettingBoolean(String key, boolean value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);

        editor.apply();
    }

    /**
     * 管理sp的long值
     */
    private static long getSystemSettingLong(String key, long defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getLong(key, defaultValue);
    }
    private static void updateSystemSettingLong(String key, long value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);

        editor.apply();
    }

    /**
     * 管理sp的string值
     */
    private static String getSystemSettingString(String key, String defaultValue) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        return preferences.getString(key, defaultValue);
    }
    private static void updateSystemSettingString(String key, String value) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(
            NAME_SYSTEM, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);

        editor.apply();
    }
}
