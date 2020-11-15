package com.yq.imageviewer.utils;

import android.os.CountDownTimer;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * 倒计时管理器
 */
public class MCountTimer extends CountDownTimer {

    private boolean mIsRunning;
    private OnTimeListener mOnTimeListener;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and
     *                          {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MCountTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void startTimer() {
        mIsRunning = true;
        super.start();
    }

    public void setTimerListener(OnTimeListener onTimeListener) {
        mOnTimeListener = onTimeListener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mOnTimeListener != null) {
            mOnTimeListener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        mIsRunning = false;
        if (mOnTimeListener != null) {
            mOnTimeListener.onFinish();
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public interface OnTimeListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }
}
