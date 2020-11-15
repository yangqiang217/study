package com.yq.eventdispatch.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import com.yq.eventdispatch.Utils;

/**
 * Created by yq on 17-3-19.
 */

public class MyListView extends ListView {

    public static final String TAG = MyListView.class.getSimpleName();

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        printLog("onInterceptTouchEvent()  event: " + Utils.getActionName(ev.getAction()) + ",  " + isIntercept);
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean pass = super.onTouchEvent(event);
        printLog("onTouchEvent()  event: " + Utils.getActionName(event.getAction()) + ",  " + pass);
        return pass;
    }

    private void printLog(String content) {
        Log.d(TAG, content);
    }
}
