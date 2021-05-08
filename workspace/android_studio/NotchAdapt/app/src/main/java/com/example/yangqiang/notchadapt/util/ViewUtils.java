package com.example.yangqiang.notchadapt.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by chengyuwang on 2016/8/18.
 */
public class ViewUtils {
    private static int ScreenHeight = -1;
    private static int ScreenWidth = -1;
    private static float itemWidth = -1F;

    private static Boolean hasNavBar = null;

    private static void initScreenSize(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
            .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(outMetrics);
        if (outMetrics.heightPixels > outMetrics.widthPixels) {
            ScreenHeight = outMetrics.heightPixels;
            ScreenWidth = outMetrics.widthPixels;
        } else {
            ScreenWidth = outMetrics.heightPixels;
            ScreenHeight = outMetrics.widthPixels;
        }
    }

    /**
     * 获取屏幕竖向长度, 该值大于屏幕宽度
     * @return
     */
    public static int getScreenHeight(Context context) {
        initScreenSize(context);
        return ScreenHeight;
    }

    public static int getScreenWidth(Context context) {
        initScreenSize(context);
        return ScreenWidth;
    }

    //获取消息栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //获取消息栏高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId =
            context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
