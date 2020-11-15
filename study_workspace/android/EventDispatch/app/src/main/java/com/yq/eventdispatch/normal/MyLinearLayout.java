package com.yq.eventdispatch.normal;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.yq.eventdispatch.Utils;

/**
 * Created by yq on 17-3-19.
 */

public class MyLinearLayout extends LinearLayout {

    public static final String TAG = "yqdispatch";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int a = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
//        a++;
//        if (a < 10) {
//            isIntercept = false;
//        } else {
//            isIntercept = true;
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isIntercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isIntercept = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                isIntercept = true;
//                break;
//        }
        printLog("MyLinearLayout onInterceptTouchEvent()  event: " + Utils.getActionName(ev.getAction()) + ",  " + isIntercept);
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        printLog("MyLinearLayout onTouchEvent()" + Utils.getActionName(event.getAction()));
        return true;
    }

    private void printLog(String content) {
        Log.d(TAG, content);
    }
}
