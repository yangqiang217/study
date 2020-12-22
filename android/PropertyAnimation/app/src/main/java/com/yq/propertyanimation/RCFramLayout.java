/*
 * Copyright 2018 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2018-04-13 23:18:56
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.yq.propertyanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 抗锯齿圆角 3dp
 */
public class RCFramLayout extends FrameLayout {
    public float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    public Path mClipPath;                 // 剪裁区域路径
    public Paint mPaint;                   // 画笔
    public Region mAreaRegion;             // 内容区域
    public RectF mLayer;

    public RCFramLayout(Context context) {
        this(context, null);
    }

    public RCFramLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setRadii(int roundCorner){
        for (int i = 0; i <8 ; i++) {
            radii[i] = roundCorner;
        }
    }

    public RCFramLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int roundCorner = 10;
        for (int i = 0; i < 8; i++) {
            radii[i] = roundCorner;
        }

        float top = 10;
        float bottom = 10;
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                radii[i] = top;
            } else {
                radii[i] = bottom;
            }
        }
        mLayer = new RectF();
        mClipPath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        try {
            mLayer.set(0, 0, w, h);
            refreshRegion(this);
        } catch (Exception e) {
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
//            onClipDraw(canvas);
            canvas.restore();
        } catch (Exception e) {
        }
    }

    private void refreshRegion(View view) {
        int w = (int) mLayer.width();
        int h = (int) mLayer.height();
        RectF areas = new RectF();
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath.reset();

        mClipPath.addRoundRect(areas, radii, Path.Direction.CW);

        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }


    private void onClipDraw(Canvas canvas) {

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawPath(mClipPath, mPaint);
        } else {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            final Path path = new Path();
            path.addRect(0, 0, (int) mLayer.width(), (int) mLayer.height(), Path.Direction.CW);
            //OP方法要求版本判断
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                path.op(mClipPath, Path.Op.DIFFERENCE);
            }
            canvas.drawPath(path, mPaint);
        }
    }

}
