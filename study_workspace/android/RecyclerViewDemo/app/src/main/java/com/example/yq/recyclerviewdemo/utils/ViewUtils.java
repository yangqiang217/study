package com.example.yq.recyclerviewdemo.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.yq.recyclerviewdemo.application.MainApplication;

/**
 * Created by chengyuwang on 2016/8/18.
 */
public class ViewUtils {
    private static int ScreenHeight = -1;
    private static int ScreenWidth = -1;
    private static float itemWidth = -1F;

    private static Boolean hasNavBar = null;

    public static int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static void disappaerPortraitView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f);
        mHiddenAction.setDuration(500);
        view.startAnimation(mHiddenAction);
        view.setVisibility(View.GONE);
    }

    public static void displayTextView(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
            mHiddenAction.setDuration(200);
            view.startAnimation(mHiddenAction);
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void showTextView(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(200);
            view.startAnimation(mShowAction);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void showLandscapeView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f);

        mHiddenAction.setDuration(300);
        view.startAnimation(mHiddenAction);
        view.setVisibility(View.VISIBLE);
    }


    private static void initScreenSize() {
        if (ScreenHeight == -1) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) MainApplication.getContext()
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
    }

    /**
     * 获取屏幕竖向长度, 该值大于屏幕宽度
     * @return
     */
    public static int getScreenHeight() {
        if (ScreenHeight == -1) {
            synchronized (ViewUtils.class) {
                initScreenSize();
            }
        }
        return ScreenHeight;
    }

    public static int getScreenWidth() {
        if (ScreenWidth == -1) {
            synchronized (ViewUtils.class) {
                initScreenSize();
            }
        }
        return ScreenWidth;
    }
}
