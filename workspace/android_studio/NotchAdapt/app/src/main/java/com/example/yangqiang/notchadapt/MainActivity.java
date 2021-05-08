package com.example.yangqiang.notchadapt;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.ViewGroup;

import com.example.yangqiang.notchadapt.util.ViewUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = findViewById(R.id.root);

        System.out.println("yqtest, sch: " + ViewUtils.getScreenHeight(this));
        System.out.println("yqtest, sth: " + ViewUtils.getStatusBarHeight(this));
        System.out.println("yqtest, nah: " + ViewUtils.getNavigationBarHeight(this));
        mRoot.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("yqtest, rootH: " + mRoot.getHeight());

//                DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
//                System.out.println("安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
//                System.out.println("安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
//                System.out.println("安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
//                System.out.println("安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());
//
//                List<Rect> rects = displayCutout.getBoundingRects();
//                if (rects == null || rects.size() == 0) {
//                    System.out.println("不是刘海屏");
//                } else {
//                    System.out.println("刘海屏数量:" + rects.size());
//                    for (Rect rect : rects) {
//                        System.out.println("刘海屏区域：" + rect);
//                    }
//                }
            }
        });
    }
}
