package com.yq.imageviewer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author zewenwang created on 2017/4/7.
 */
public class PreferenceUtil {

    /**
     * 往默认的SharedPreferences中写入数据
     */
    public static synchronized void setToDefault(Context context, String key, String value) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().putString(key, value).commit();
    }

    public static synchronized void setToDefault(Context context, Object... params) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        setTo(preference, params);
    }

    public static synchronized void setTo(Context context, String prefName, Object... params) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        setTo(preference, params);
    }

    private static synchronized void setTo(SharedPreferences preference, Object... params) {
        if (preference == null) {
            return;
        }
        if (params.length == 0 || params.length % 2 != 0) {
            throw new RuntimeException("params need key value pairs");
        }
        SharedPreferences.Editor editor = preference.edit();
        for (int i = 0; i < params.length - 1; i += 2) {
            String key = params[i].toString();
            Object value = params[i + 1];
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    /**
     * 从默认的SharedPreferences中读取数据
     */
    public static synchronized String getFromDefault(Context context, String key) {
        if (context == null) {
            return "";
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString(key, "");
    }

    public static synchronized <T> T getFromDefault(Context context, String key, T defValue) {
        if (context == null) {
            return defValue;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return getFrom(preference, key, defValue);
    }

    public static synchronized <T> T getFrom(Context context, String prefName, String key, T defValue) {
        if (context == null) {
            return defValue;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return getFrom(preference, key, defValue);
    }

    private static synchronized <T> T getFrom(SharedPreferences preference, String key, T defValue) {
        if (preference == null) {
            return defValue;
        }
        String value = preference.getString(key, defValue.toString());
        try {
            return (T) ReflectUtils.invokeStaticMethod(defValue.getClass(), "valueOf",
                new Class[]{String.class}, new Object[]{value});
        } catch (Exception e) {
        }
        try {
            return (T) value;
        } catch (Exception e) {
        }
        return defValue;
    }

    public static synchronized void removeFromDefault(Context context, String key) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().remove(key).commit();
    }

    public static synchronized void removeFrom(Context context, String prefName, String key) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        preference.edit().remove(key).commit();
    }

    public static synchronized void clearDefault(Context context) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().clear().commit();
    }

    public static synchronized void clear(Context context, String prefName) {
        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        SharedPreferences preference = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        preference.edit().clear().commit();
    }

}
