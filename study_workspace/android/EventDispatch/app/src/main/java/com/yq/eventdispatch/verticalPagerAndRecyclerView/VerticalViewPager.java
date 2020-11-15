package com.yq.eventdispatch.verticalPagerAndRecyclerView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("vv down");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("vv move");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("vv up");
                break;
        }
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
//
//        //以下这些自己加的，是为了解决播放页面exoplayer只能收到down事件而不好处理单击暂停的问题（暂停一般都是up事件做的）
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                intercepted = false;
//                mStartX = ev.getX();
//                mStartY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float dis = distance(ev.getX(), ev.getY());
//                intercepted = (dis > THRESHOLD_INTERCEPT_TOUCH_EVENT);
//                break;
//            case MotionEvent.ACTION_UP:
//                intercepted = false;//必须是false。如果距离超了阈值，move被拦截，up也不会给子view，但距离不够，必须给子view up事件
//                break;
//        }
        System.out.println("vv intercept: " + intercepted);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

}