package com.yq.eventdispatch;

import android.view.MotionEvent;

/**
 * Created by yq on 17-3-19.
 */

public class Utils {

    private static final String ACTION_DOWN = "ACTION_DOWN";//0
    private static final String ACTION_UP = "ACTION_UP";//1
    private static final String ACTION_MOVE = "ACTION_MOVE";//2
    private static final String ACTION_CANCEL = "ACTION_CANCEL";//2

    public static String getActionName(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return ACTION_DOWN;
            case MotionEvent.ACTION_UP:
                return ACTION_UP;
            case MotionEvent.ACTION_MOVE:
                return ACTION_MOVE;
            case MotionEvent.ACTION_CANCEL:
                return ACTION_CANCEL;
            default:
                return action + "";
        }
    }
}
