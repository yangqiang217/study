package com.yq.imageviewer.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author zewenwang created on 2017/3/12.
 */
public class AppUtil {
    /**
     * 强制收起输入法面板
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) {
            return;
        }

        View curfocus = activity.getCurrentFocus();
        if (null == curfocus) {
            return;
        }

        IBinder windowToken = curfocus.getWindowToken();
        if (null == windowToken) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制收起输入法面板
     */
    public static void hideSoftInput(View view) {
        if (view == null) {
            return;
        }

        IBinder windowToken = view.getWindowToken();
        if (null == windowToken) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制打开输入法面板
     *
     * @param context
     * @param view
     */
    public static void showSoftKeyBoard(Context context, View view) {
        if (context == null) {
            return;
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 强制打开输入法面板
     *
     * @param context
     */
    public static void showSoftKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * dp转换成px
     *
     * @param context
     * @param dp
     */
    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            context.getResources().getDisplayMetrics());
    }

    /**
     * dp转换成px
     *
     * @param context
     * @param sp
     */
    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
            context.getResources().getDisplayMetrics());
    }
}
