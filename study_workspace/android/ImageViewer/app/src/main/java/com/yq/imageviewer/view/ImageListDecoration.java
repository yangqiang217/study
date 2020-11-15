package com.yq.imageviewer.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangqiang on 11/02/2018.
 */

public class ImageListDecoration extends RecyclerView.ItemDecoration {

    private int mDividerSize;
    private Paint mPaint;

    public ImageListDecoration(int space, int color) {
        mDividerSize = space;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent,
        RecyclerView.State state) {

        // 这里获取的是一屏的Item数量
        int childCount = parent.getChildCount();

        // 分割线从Item的底部开始绘制，且在最后一个Item底部不绘制
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                (RecyclerView.LayoutParams) child.getLayoutParams();
            // 有的Item布局会设置layout_marginXXX
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerSize;
            int left = child.getLeft() + layoutParams.leftMargin;
            int right = child.getRight() - layoutParams.rightMargin;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        outRect.set(0, 0, 0, mDividerSize);
    }
}
