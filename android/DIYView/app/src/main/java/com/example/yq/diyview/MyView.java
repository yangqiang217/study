package com.example.yq.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yq on 2017/3/8.
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int width = getMySize(100, widthMeasureSpec);
//        int height = getMySize(100, heightMeasureSpec);
//
//        if (width < height) {
//            height = width;
//        } else {
//            width = height;
//        }
//
//        setMeasuredDimension(width, height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        int finalSize = 0;
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                finalSize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                finalSize = size;
                break;
            case MeasureSpec.EXACTLY:
                finalSize = size;
                break;
            default: break;
        }

        return finalSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int r = getMeasuredHeight() / 2;
        int centerX = getLeft() + r;
        int centerY = getTop() + r;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        canvas.drawCircle(centerX, centerY, r, paint);
    }
}
