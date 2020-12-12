package com.yq.testanimation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button btnTranslation;
    private Button btnBackgroundColor;
    private Button btnAnimationSet;
    private Button btnXmlAnimation;

    //让button加宽的两种方法
    private Button btnWiden;
    private Button btnWidenWrapper;//1
    private Button btnWidenValueAnimator;//2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnTranslation = (Button) findViewById(R.id.btnTranslation);
        btnBackgroundColor = (Button) findViewById(R.id.btnBackgroundColor);
        btnAnimationSet = (Button) findViewById(R.id.btnAnimationSet);
        btnXmlAnimation = (Button) findViewById(R.id.btnXmlAnimation);

        btnWiden = (Button) findViewById(R.id.btnWiden);
        btnWidenWrapper = (Button) findViewById(R.id.btnWidenWrapper);
        btnWidenValueAnimator = (Button) findViewById(R.id.btnWidenValueAnimator);

        MyClickListener myClickListener = new MyClickListener();
        btnTranslation.setOnClickListener(myClickListener);
        btnBackgroundColor.setOnClickListener(myClickListener);
        btnAnimationSet.setOnClickListener(myClickListener);
        btnXmlAnimation.setOnClickListener(myClickListener);
        btnWiden.setOnClickListener(myClickListener);
        btnWidenWrapper.setOnClickListener(myClickListener);
        btnWidenValueAnimator.setOnClickListener(myClickListener);

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 平移
     * @param view
     */
    private void animTranslation(View view) {
        ObjectAnimator.ofFloat(view, "translationX", view.getWidth()).start();
    }

    /**
     * 变换背景色，3s内从a到b的变换
     * @param view
     */
    private void animBackgroundColor(View view) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    /**
     * 5s内对旋转/平移/缩放/透明度都变换
     */
    private void animSet(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(view, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(view, "rotation", 0, -90),
                ObjectAnimator.ofFloat(view, "translationX", 0, 90),
                ObjectAnimator.ofFloat(view, "translationY", 0, 90),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0.25f, 1)
        );
        set.setDuration(5000).start();
    }

    /**
     * xml动画
     */
    private void animXML(View view) {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.property_animator);
        set.setTarget(view);
        set.start();
    }

    /**
     * 加宽button方法1，包装wrapper
     */
    private void animButtonWiden1() {
        ViewWrapper viewWrapper = new ViewWrapper(btnWiden);
        ObjectAnimator.ofInt(viewWrapper, "width", btnWiden.getWidth(), 500).setDuration(3000).start();//ofInt的多个数字表示将此加宽分为多个阶段，然后把时间按阶段平分，比如1,50,1000分俩阶段，每阶段1.5s
    }

    /**
     * 加宽button方法2，用valueAnimation
     */
    private void animButtonWiden2(final View view, final int start, final int end) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private IntEvaluator evaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前进度
                int currentValue = (int) animation.getAnimatedValue();
                Log.d(TAG, "current value: " + currentValue);

                //当前进度占整个动画过程的比例，浮点，0-1之间
                float fraction = animation.getAnimatedFraction();
                //调用整型估值器，通过比例计算宽度
                view.getLayoutParams().width = evaluator.evaluate(fraction, start, end);
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(3000).start();
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnTranslation:
                    animTranslation(btnTranslation);
                    break;
                case R.id.btnBackgroundColor:
                    animBackgroundColor(btnBackgroundColor);
                    break;
                case R.id.btnAnimationSet:
                    animSet(btnAnimationSet);
                    break;
                case R.id.btnXmlAnimation:
                    animXML(btnXmlAnimation);
                    break;
                case R.id.btnWidenWrapper:
                    animButtonWiden1();
                    break;
                case R.id.btnWidenValueAnimator:
                    animButtonWiden2(btnWiden, btnWiden.getWidth(), 500);
                    break;
            }
        }
    }

    private class ViewWrapper {
        private View target;

        public ViewWrapper(View target) {
            this.target = target;
        }

        public int getWidth() {
            return target.getLayoutParams().width;
        }

        public void setWidth(int width) {
            target.getLayoutParams().width = width;
            target.requestLayout();
        }
    }
}
