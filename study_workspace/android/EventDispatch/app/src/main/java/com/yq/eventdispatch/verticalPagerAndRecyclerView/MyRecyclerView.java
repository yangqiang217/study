package com.yq.eventdispatch.verticalPagerAndRecyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yq on 2017/5/15.
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("recycler down");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("recycler move");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("recycler up");
                break;
        }
        VerticalViewActivity.verticalViewPager.requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }
}
