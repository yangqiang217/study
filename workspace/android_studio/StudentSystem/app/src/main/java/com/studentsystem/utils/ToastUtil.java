package com.studentsystem.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 封装一个简单的showToast的类
 */
public class ToastUtil {

    public static void showToast(Context context, String content) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
