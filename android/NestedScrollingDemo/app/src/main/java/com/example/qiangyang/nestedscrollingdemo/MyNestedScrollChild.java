package com.example.qiangyang.nestedscrollingdemo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.support.v4.view.NestedScrollingChild;

/**
 * Created by qiangyang on 2017/8/14.
 */

public class MyNestedScrollChild extends LinearLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] offset = new int[2];//偏移量
    private final int[] comsumed = new int[2];//消费
    private int lastY;
    private int showHeight;

    public MyNestedScrollChild(Context context) {
        super(context);
    }

    public MyNestedScrollChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一次测量，因为布局文件中高度是wrap_content，因此测量模式为atmost，即高度不超过父控件的剩余空间
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showHeight = getMeasuredHeight();

        //第二次测量，对稿哦度没有任何限制，那么测量出来的就是完全展示内容所需要的高度
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getRawY();
                int dy = y - lastY;
                lastY = y;
                if (startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL) && dispatchNestedPreScroll(0, dy, comsumed, offset)) {
                    //如果找到了支持嵌套滑动的父类,父类进行了一系列的滑动
                    int remain = dy - comsumed[1];
                    System.out.println("找到了, dy: " + dy + ", remain: " + remain);
                    if (remain != 0) {
                        scrollBy(0, -remain);
                    }
                } else {
                    System.out.println("没找到");
                    scrollBy(0, -dy);
                }
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        int maxY = getMeasuredHeight() - showHeight;
        if (y > maxY) {
            y = maxY;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mNestedScrollingChildHelper == null) {
            mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
            mNestedScrollingChildHelper.setNestedScrollingEnabled(true);
        }
        return mNestedScrollingChildHelper;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

//    @Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
//    }
//
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return getScrollingChildHelper().isNestedScrollingEnabled();
//    }
//
//
//
//    @Override
//    public void stopNestedScroll() {
//        getScrollingChildHelper().stopNestedScroll();
//    }
//
//    @Override
//    public boolean hasNestedScrollingParent() {
//        return getScrollingChildHelper().hasNestedScrollingParent();
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
//        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//    }
//
//
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
//    }
}
