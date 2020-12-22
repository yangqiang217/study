package com.yq.propertyanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import static android.view.View.LAYER_TYPE_HARDWARE;

public class RCTestActivity extends AppCompatActivity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_c_test);


        mTv = findViewById(R.id.tv);

//        mTv.setLayerType(LAYER_TYPE_HARDWARE, null);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void start() {
        ObjectAnimator animatorT = ObjectAnimator.ofFloat(mTv, "translationX", 0, 300);
        animatorT.setDuration(500);

        ObjectAnimator animatorA = ObjectAnimator.ofFloat(mTv, "alpha", 1, 0);
        animatorA.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorA).after(animatorT);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mTv.setAlpha(1);
                mTv.setTranslationX(0);
            }
        });
        animatorSet.start();
    }
}