package com.yq.imageviewer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yq.imageviewer.R;

/**
 * Created by yangqiang on 2018/4/19.
 */

public class LoadingImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DURATION = 1000;

    private ObjectAnimator mObjectAnimator;

    public LoadingImageView(Context context) {
        super(context);
        init();
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(R.mipmap.loading);
    }

    public void show() {
        setVisibility(VISIBLE);
        mObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        mObjectAnimator.setDuration(DURATION);
        mObjectAnimator.start();
    }

    public void hide() {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        setVisibility(GONE);
    }
}
