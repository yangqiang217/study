package com.yq.eventdispatch.normal;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import com.yq.eventdispatch.Utils;

/**
 * Created by yq on 17-3-19.
 */

public class MyTextView extends AppCompatTextView {

    public static final String TAG = MyLinearLayout.TAG;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    boolean deal = true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean pass = super.onTouchEvent(event);
//        getParent().requestDisallowInterceptTouchEvent(true);
        printLog("MyTextView onTouchEvent()  event: " + Utils.getActionName(event.getAction()) + ",  " + pass);
        if(deal) {
            deal = false;
            return true;
        } else {
            return false;
        }
    }

    private void printLog(String content) {
        Log.d(TAG, content);
    }
}
