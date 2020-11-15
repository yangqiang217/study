package com.example.yq.moveasfinger.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yq on 2017/3/5.
 */

public class MyViews extends LinearLayout {

    private Context context;
    private MyViews myViews;

    public MyViews(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        myViews = this;

        TextView view = addHead();
        addHeadTouchEvent(view);
        addViews();
    }

    private void addHeadTouchEvent(TextView view) {
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int y = 0, deltaY = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        deltaY = (int) (event.getY() - y);
                        System.out.println("move: " + deltaY);
                        //method 1:
//                        myViews.setY(myViews.getY() + deltaY);
                        //method 2:
                        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
                        layoutParams.topMargin = (int) (event.getRawY() + deltaY);
                        v.setLayoutParams(layoutParams);

                        break;
                    case MotionEvent.ACTION_DOWN:
                        y = (int) event.getY();
                        break;
                }
                return true;
            }
        });
    }

    private TextView addHead() {
        TextView view = new TextView(context);
        view.setBackgroundColor(Color.BLACK);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 150);
        view.setLayoutParams(layoutParams);
        addView(view);

        return view;
    }

    private void addViews() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 30;
        for (int i = 0; i < 5; i++) {
            Button btn = new Button(context);
            btn.setLayoutParams(layoutParams);
            btn.setText(i + "");
            addView(btn);
        }
    }
}
