package com.yq.eventdispatch.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by yq on 17-3-19.
 */

public class MyTextView extends TextView {

    public static final String TAG = MyTextView.class.getSimpleName();

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean pass = super.onTouchEvent(event);
        printLog("onTouchEvent: " + pass);
        return pass;
    }

    private void printLog(String content) {
        Log.d(TAG, content);
    }
}
