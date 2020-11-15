package com.example.dragrect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by YangQiang on 2016/5/27.
 */
public class DragRectView extends View {

    private Paint mPaint;
    private int startX, startY;//起始点
    private int left, top, right, bottom;

    private OnTouchUpListener onTouchUpListener;

    public DragRectView(Context context) {
        super(context);
        init();
    }

    public DragRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        this.setOnTouchListener(new OnDragTouchListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("ondraw, left:" + left + ", top:" + top + ", right:" + right + ", " +
                "bottom:" + bottom);
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 开始进入框选模式
     */
    public void begin() {
        this.setVisibility(View.VISIBLE);
    }

    /**
     * 结束框选模式
     */
    public void stop() {
        this.setVisibility(View.GONE);
    }

    /**
     * 暂时让框消失，但是还是框选模式
     */
    public void hide() {
        left = 0;
        top = 0;
        right = 0;
        bottom = 0;
        invalidate();
    }

    public void setStartLocation(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void changeSize(int endX, int endY) {
        if (startX < endX) {
            left = startX;
            right = endX;
        } else {
            left = endX;
            right = startX;
        }

        if (startY < endY) {
            top = startY;
            bottom = endY;
        } else {
            top = endY;
            bottom = startY;
        }
        invalidate();
    }

    public void setOnTouchUpListener(OnTouchUpListener onTouchUpListener) {
        this.onTouchUpListener = onTouchUpListener;
    }

    private class OnDragTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setStartLocation((int) event.getX(), (int) event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    changeSize((int) event.getX(), (int) event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUpListener.onTouchUp(left, top, right, bottom);
                    hide();
                    break;
            }
            return true;
        }
    }

    /**
     * 手指抬起后给的回调
     */
    public interface OnTouchUpListener {
        void onTouchUp(int left, int top, int right, int bottom);
    }
}
