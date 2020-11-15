package com.example.yq.recyclerviewdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangqiang on 2018/5/9.
 */

public class MyDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private float mNodeRadius = 20;
    private int mOffsetTop = 10;
    private int mOffsetLeft = 80;

    public MyDecoration() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = 30;
        }
    }

    /**
     * 看源码是在绘制list元素之前绘制的，所以这里调用draw会被list 覆盖，也就是绘制的东西在每个item下面
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        draw(c, parent, state);
    }

    /**
     * 看源码是在绘制list元素之后绘制的，绘制的东西在每个item上面
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        draw(c, parent, state);
    }

    private void draw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        System.out.println("--------");
        for ( int i = 0; i < childCount; i++ ) {
            View view = parent.getChildAt(i);

            int index = parent.getChildAdapterPosition(view);
            System.out.println("index: " + index);
            float dividerTop = view.getTop() - mOffsetTop;
            //第一个ItemView 没有向上方向的间隔
            if ( index == 0 ) {
                dividerTop = view.getTop();
            }

            float dividerLeft = parent.getPaddingLeft();
            float dividerBottom = view.getBottom();
            float dividerRight = parent.getWidth() - parent.getPaddingRight();

            float centerX = dividerLeft + mOffsetLeft / 2;
            float centerY = dividerTop + (dividerBottom - dividerTop) / 2;

            float upLineTopX = centerX;
            float upLineTopY = dividerTop;
            float upLineBottomX = centerX;
            float upLineBottomY = centerY - mNodeRadius;

            //绘制上半部轴线
            c.drawLine(upLineTopX,upLineTopY,upLineBottomX,upLineBottomY,mPaint);

            //绘制时间轴结点
            c.drawCircle(centerX,centerY,mNodeRadius,mPaint);

            float downLineTopX = centerX;
            float downLineTopY = centerY + mNodeRadius;
            float downLineBottomX = centerX;
            float downLineBottomY = dividerBottom;

            //绘制上半部轴线
            c.drawLine(downLineTopX,downLineTopY,downLineBottomX,downLineBottomY,mPaint);
        }
    }
}
