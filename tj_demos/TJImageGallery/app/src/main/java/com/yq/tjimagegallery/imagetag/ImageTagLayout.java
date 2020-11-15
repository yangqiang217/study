package com.yq.tjimagegallery.imagetag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.yq.tjimagegallery.ImageItemDataInfo;
import com.yq.tjimagegallery.R;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class ImageTagLayout extends AbsoluteLayout {
    private MyImageView mImageView;

    private GestureDetector tagViewGesture;

    /** 当前正在操作的EditText */
    public TagView mCurrTouchTag;
    public TagView mCurrSelectedTag;

    public ArrayList<TagView> tagViewList = new ArrayList<TagView>();

    private static final int AUTO_SCALE_TIME_GAP = 10;

    public ImageTagLayout(Context context) {
        this(context, null);
    }

    public ImageTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mImageView = new MyImageView(getContext());
        addView(mImageView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    public void loadImageAndTags(Bitmap bmp, ImageItemDataInfo data, boolean hideTags) {
        if (bmp == null) {
            mImageView.setScaleType(ScaleType.CENTER);
            mImageView.setImageResource(R.drawable.image_missed);
        } else {
            mCurrRotateAngle = data.mRotate;
            mImageView.setImageBitmap(bmp);
            if (hideTags) {
                return;
            }
            TagView tag = null;
            ImageItemDataInfo.PicTagInfo info = null;
            for (int i = 0; i < data.mTagArray.size(); i++) {
                info = data.mTagArray.get(i);
                tag = new TagView(getContext());
                tag.setText(info.getmName());
                tag.mXRate = info.getmLocX();
                tag.mYRate = info.getmLocY();
                tagViewList.add(tag);
            }
        }
    }

    private void initTheEvent() {
        /**
         * 对底图长按,缩放,移动等的响应
         */
        mImageView.setMytouchListener(new MyTouchListener() {

            @Override
            public void theMove(MotionEvent event, float dx, float dy) {// 平移
                refreshTheTags(false);
            }

            /**
             * 缩放变化
             */
            @Override
            public void myScallChange(float oriX, float oriY) {
                refreshTheTags(false);
            }
        });

        /***
         * 对文本框的手势 主要是长按删除
         */
        tagViewGesture = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mCurrTouchTag != mCurrSelectedTag) {
                            if (mCurrSelectedTag != null) {
                                mCurrSelectedTag.setSelected(false);
                            }
                            mCurrTouchTag.setSelected(true);
                            mCurrSelectedTag = mCurrTouchTag;
                            if (mCallback != null) {
                                mCallback.onTagSelected(mCurrSelectedTag);
                            }
                        } else {
                            if (mCurrTouchTag.isSelected()) {
                                LayoutParams lp = (LayoutParams) mCurrTouchTag
                                        .getLayoutParams();
                                if (lp.x >= mCurrTouchTag.getWidth()
                                        && mCurrTouchTag.mTextToRightOfCircle) {
                                    mCurrTouchTag.textToRightOfCircle(false);
                                    lp.x -= mCurrTouchTag.getWidth();
                                    mCurrTouchTag.setLayoutParams(lp);
                                } else if (getWidth() - lp.x >= 2 * mCurrTouchTag.getWidth()
                                        && !mCurrTouchTag.mTextToRightOfCircle) {
                                    mCurrTouchTag.textToRightOfCircle(true);
                                    lp.x += mCurrTouchTag.getWidth();
                                    mCurrTouchTag.setLayoutParams(lp);
                                }
                            } else {
                                mCurrTouchTag.setSelected(true);
                                mCurrSelectedTag = mCurrTouchTag;
                                if (mCallback != null) {
                                    mCallback.onTagSelected(mCurrSelectedTag);
                                }
                            }
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }
    
    private boolean posInPixel(MotionEvent event) {
        if (event == null) {
            return false;
        }
        RectF myRectF = mImageView.getMartixRectF();
        float touchX = event.getX();
        float touchY = event.getY();

        if (touchX < myRectF.left) {
            return false;
        }
        if (touchX > myRectF.right) {
            return false;
        }
        if (touchY < myRectF.top) {
            return false;
        }
        if (touchY > myRectF.bottom) {
            return false;
        }
        
        return true;
    }

    public void addTag(MotionEvent event) {
        if (event == null) {
            return;
        }
        RectF myRectF = mImageView.getMartixRectF();
        float touchX = event.getX();
        float touchY = event.getY();

        if (touchX < myRectF.left) {
            return;
        }
        if (touchX > myRectF.right) {
            return;
        }
        if (touchY < myRectF.top) {
            return;
        }
        if (touchY > myRectF.bottom) {
            return;
        }

        float gapX = event.getX() - myRectF.left;
        float gapY = event.getY() - myRectF.top;

        float xRate = gapX / myRectF.width();// 相对图的位置坐标 比例坐标
        float yRate = gapY / myRectF.height();
        float tmp = 0;

        if (mCurrRotateAngle == 90) {
            tmp = xRate;
            xRate = yRate;
            yRate = 1 - tmp;
        } else if (mCurrRotateAngle == 180) {
            xRate = 1 - xRate;
            yRate = 1 - yRate;
        } else if (mCurrRotateAngle == 270) {
            tmp = xRate;
            xRate = 1 - yRate;
            yRate = tmp;
        }

        TagView tag = new TagView(getContext());
        tag.mXRate = xRate;
        tag.mYRate = yRate;

        float[] pts = new float[2];
        pts[0] = event.getX();
        pts[1] = event.getY();
        mImageView.getMatrix().mapPoints(pts);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, (int) pts[0],
                (int) (pts[1] - tag.getCurrHeight() / 2));

        tag.mShowDotBg = true;

        if (lp.x > getWidth() / 2) {
            tag.textToRightOfCircle(false);
            lp.x -= tag.getCurrWidth();
        } else {
            tag.textToRightOfCircle(true);
        }

        tag.setOnTouchListener(new TagViewTouchListener());

        addView(tag, lp);

        for (TagView t : tagViewList) {
            t.setVisibility(View.INVISIBLE);
        }
        tagViewList.add(tag);
        
        mCurrTouchTag = tag;
    }

    private void refreshTheTags(boolean isRotate) {
        float[] pts = new float[2];
        for (TagView tag : tagViewList) {
            pts[0] = tag.mXRate * mImageView.mBitmap.getWidth();
            pts[1] = tag.mYRate * mImageView.mBitmap.getHeight();
            mImageView.getImageMatrix().mapPoints(pts);
            LayoutParams lp = (LayoutParams) tag.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, (int) pts[0],
                        (int) (pts[1] - tag.getHeight() / 2));
            } else {
                lp.x = (int) pts[0];
                lp.y = (int) (pts[1] - tag.getHeight() / 2);
            }
            
            if (isRotate) {
                if (lp.x > getWidth() / 2) {
                    tag.textToRightOfCircle(false);
                } else {
                    tag.textToRightOfCircle(true);
                }
            }
            
            if (!tag.mTextToRightOfCircle) {
                lp.x -= tag.getWidth();
            }
            
            float hRate = tag.mXRate;
            float vRate = tag.mYRate;
            float hSize = mImageView.mBitmap.getWidth();
            float vSize = mImageView.mBitmap.getHeight();
            if (mCurrRotateAngle == 90) {
                hRate = 1 - tag.mYRate;
                vRate = tag.mXRate;
                hSize = mImageView.mBitmap.getHeight();
                vSize = mImageView.mBitmap.getWidth();
            } else if (mCurrRotateAngle == 180) {
                hRate = 1 - tag.mXRate;
                vRate = 1 - tag.mYRate;
            } else if (mCurrRotateAngle == 270) {
                hRate = tag.mYRate;
                vRate = 1 - tag.mXRate;
                hSize = mImageView.mBitmap.getHeight();
                vSize = mImageView.mBitmap.getWidth();
            }
            
            if (tag.mTextToRightOfCircle) {
                int adjust = (int) (tag.getDotSize() - (1 - hRate) * hSize);
                if (adjust > 0) {
                    lp.x -= adjust;
                }
            } else {
                int adjust = (int) (tag.getDotSize() - hRate * hSize);
                if (adjust > 0) {
                    lp.x += adjust;
                }
            }
            
            if (vRate < 0.5) {
                int adjust = (int) (tag.getHeight() / 2 - vRate * vSize);
                if (adjust > 0) {
                    lp.y += adjust;
                }
            } else {
                int adjust = (int) (tag.getHeight() / 2 - (1 - vRate) * vSize);
                if (adjust > 0) {
                    lp.y -= adjust;
                }
            }
            
            tag.setLayoutParams(lp);
        }
    }

    /**
     * 下面这是响应文本框的拖动事件
     */
    private int lastPointerCount = 0;
    private boolean mTextMoving;

    private class TagViewTouchListener implements OnTouchListener {
        private float myLastX;
        private float myLastY;
        private float pts[] = new float[2];

        public boolean onTouch(View v, MotionEvent event) {
            mCurrTouchTag = (TagView) v;
            if (tagViewGesture.onTouchEvent(event)) {
                return true;
            }
            TagView tag = (TagView) v;
            //
            float x = 0, y = 0;
            final int pointCount = event.getPointerCount();
            if (pointCount > 1) {
                return false;// 多于1个手指就无视了
            }
            x = event.getRawX() - ImageTagLayout.this.getLeft();
            y = event.getRawY() - ImageTagLayout.this.getTop();
            /**
             * 当每次触摸点变化时
             */
            if (pointCount != lastPointerCount) {
                if (pointCount > 1) {
                    return false;
                }
            }
            lastPointerCount = pointCount;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myLastX = x;
                myLastY = y;
                mTextMoving = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - myLastX;
                float dy = y - myLastY;

                LayoutParams lp = (LayoutParams) tag.getLayoutParams();

                if (dx < 0 && lp.x + dx < 0) {
                    break;
                }
                if (dx > 0 && lp.x + dx >= getWidth() - tag.getWidth()) {
                    break;
                }
                if (dy > 0 && lp.y + dy >= getHeight() - tag.getHeight()) {
                    break;
                }
                if (dy < 0 && lp.y + dy < 0) {
                    break;
                }

                // 重新设定点的相对画位置
                // 主要是缩放后图片矩形会变化
                RectF theRect = mImageView.getMartixRectF();
                float dxRate = dx / theRect.width();
                float dyRate = dy / theRect.height();
                float tmpXRate = 0;
                float tmpYRate = 0;
                if (mCurrRotateAngle == 0) {
                    tmpXRate = tag.mXRate + dxRate;
                    tmpYRate = tag.mYRate + dyRate;
                } else if (mCurrRotateAngle == 90) {
                    tmpXRate = tag.mXRate + dyRate;
                    tmpYRate = tag.mYRate - dxRate;
                } else if (mCurrRotateAngle == 180) {
                    tmpXRate = tag.mXRate - dxRate;
                    tmpYRate = tag.mYRate - dyRate;
                } else if (mCurrRotateAngle == 270) {
                    tmpXRate = tag.mXRate - dyRate;
                    tmpYRate = tag.mYRate + dxRate;
                }

                myLastX = x;
                myLastY = y;

                if (tmpXRate > 0 && tmpXRate < 1 && tmpYRate > 0
                        && tmpYRate < 1) {
                    tag.mXRate = tmpXRate;
                    tag.mYRate = tmpYRate;
                    pts[0] = tag.mXRate * mImageView.mBitmap.getWidth();
                    pts[1] = tag.mYRate * mImageView.mBitmap.getHeight();
                    mImageView.getImageMatrix().mapPoints(pts);
                    lp = (LayoutParams) tag.getLayoutParams();
                    lp.x = (int) pts[0];
                    lp.y = (int) (pts[1] - tag.getHeight() / 2);
                    if (!tag.mTextToRightOfCircle) {
                        lp.x -= tag.getWidth();
                    }
                    
                    float hRate = tag.mXRate;
                    float vRate = tag.mYRate;
                    float hSize = mImageView.mBitmap.getWidth();
                    float vSize = mImageView.mBitmap.getHeight();
                    if (mCurrRotateAngle == 90) {
                        hRate = 1 - tag.mYRate;
                        vRate = tag.mXRate;
                        hSize = mImageView.mBitmap.getHeight();
                        vSize = mImageView.mBitmap.getWidth();
                    } else if (mCurrRotateAngle == 180) {
                        hRate = 1 - tag.mXRate;
                        vRate = 1 - tag.mYRate;
                    } else if (mCurrRotateAngle == 270) {
                        hRate = tag.mYRate;
                        vRate = 1 - tag.mXRate;
                        hSize = mImageView.mBitmap.getHeight();
                        vSize = mImageView.mBitmap.getWidth();
                    }
                    
                    if (tag.mTextToRightOfCircle) {
                        int adjust = (int) (tag.getDotSize() - (1 - hRate) * hSize);
                        if (adjust > 0) {
                            lp.x -= adjust;
                        }
                    } else {
                        int adjust = (int) (tag.getDotSize() - hRate * hSize);
                        if (adjust > 0) {
                            lp.x += adjust;
                        }
                    }

                    if (vRate < 0.5) {
                        int adjust = (int) (tag.getDotSize() / 2 - vRate * vSize);
                        if (adjust > 0) {
                            lp.y += adjust;
                        }
                    } else {
                        int adjust = (int) (tag.getDotSize() / 2 - (1 - vRate) * vSize);
                        if (adjust > 0) {
                            lp.y -= adjust;
                        }
                    }
                    
                    tag.setLayoutParams(lp);
                    if (mCallback != null) {
                        mCallback.onTagMoved();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTextMoving = false;
                break;
            }
            return true;
        }

    }

    private int mCurrRotateAngle = 0;

    public int getCurrRotateAngle() {
        return mCurrRotateAngle;
    }

    public void rotateImage() {
        if (mImageView.mBitmap == null) {
            return;
        }

        mCurrRotateAngle += 90;
        mCurrRotateAngle %= 360;

        Matrix matrix = mImageView.getMyMartix();
        float scale = mImageView.initScale;
        if (mCurrRotateAngle == 90 || mCurrRotateAngle == 270) {
            scale = mImageView.hInitScale;
        }

        RectF rectF = mImageView.getMartixRectF();
        float deltaX = getWidth() / 2 - rectF.centerX();
        float deltaY = getHeight() / 2 - rectF.centerY();
        matrix.postTranslate(deltaX, deltaY);

        matrix.postRotate(90, mImageView.getWidth() / 2,
                mImageView.getHeight() / 2);

        matrix.postScale(scale / mImageView.getScale(), scale / mImageView.getScale(), getWidth() / 2,
                getHeight() / 2);

        mImageView.setImageMatrix(matrix);
        refreshTheTags(true);
    }

    private void loadTags(ArrayList<TagView> list) {
        float[] pts = new float[2];
        for (TagView tag : list) {
            if (tag.getParent() == null) {
                pts[0] = tag.mXRate * mImageView.mBitmap.getWidth();
                pts[1] = tag.mYRate * mImageView.mBitmap.getHeight();
                mImageView.getImageMatrix().mapPoints(pts);
    
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, (int) pts[0],
                        (int) (pts[1] - tag.getCurrHeight() / 2));
                tag.setOnTouchListener(new TagViewTouchListener());
                if (lp.x > getWidth() / 2) {
                    tag.textToRightOfCircle(false);
                    lp.x -= tag.getCurrWidth();
                } else {
                    tag.textToRightOfCircle(true);
                }
                addView(tag, lp);
                tag.forceWrapContent(tag.getText().toString());
            }
        }
    }

    public class MyImageView extends ImageView implements
            OnScaleGestureListener, OnTouchListener, OnGlobalLayoutListener {
        private static final float SCALE_MAX = 4.0f;
        private static final float SCALE_MIN = 2.0f;
        private float initScale = 1.0f;
        private float hInitScale = 1.0f;

        /**
         * 用于缩放手势检测
         */
        private ScaleGestureDetector mScaleGestureDetector = null;
        private Matrix mScaleMatrix = new Matrix();

        /**
         * 用于双击检测 和 长按事件
         */
        private GestureDetector mGestureDetector;
        private boolean isAutoScale;

        private float mLastX;
        private float mLastY;

        private int lastPointerCount = 0;

        private boolean isCheckTopAndBottom = true;
        private boolean isCheckLeftAndRight = true;

        public MyImageView(Context context) {
            this(context, null);
        }

        public MyImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            super.setScaleType(ScaleType.MATRIX);
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onDoubleTapEvent(MotionEvent e) {
                            if (isAutoScale || !posInPixel(e))
                                return true;
                            float x = e.getX();
                            float y = e.getY();
                            float tInitScale = initScale;
                            if (mCurrRotateAngle == 90 || mCurrRotateAngle == 270) {
                                tInitScale = hInitScale;
                            }
                            if (getScale() < SCALE_MIN * tInitScale) {
                                MyImageView.this.postDelayed(
                                        new AutoScaleRunnable(SCALE_MIN * tInitScale, x, y),
                                        AUTO_SCALE_TIME_GAP);
                                isAutoScale = true;
                            } else if (getScale() >= SCALE_MIN * tInitScale
                                    && getScale() < SCALE_MAX * tInitScale) {
                                MyImageView.this.postDelayed(
                                        new AutoScaleRunnable(SCALE_MAX * tInitScale,
                                                x, y), AUTO_SCALE_TIME_GAP);// 放大两倍
                                isAutoScale = true;
                            } else {// 比max大 先这样写待会儿回来 看看有没反
                                MyImageView.this.postDelayed(
                                        new AutoScaleRunnable(tInitScale, x, y),
                                        AUTO_SCALE_TIME_GAP);// 先这样吧如果比最大的大就再点返回
                                isAutoScale = true;
                            }
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent event) {
                            super.onLongPress(event);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            if (posInPixel(e)) {
                                if (mCallback != null) {
                                    mCallback.onImageClick(e);
                                }
                            }
                            return super.onSingleTapConfirmed(e);
                        }
                    });
            mScaleGestureDetector = new ScaleGestureDetector(context, this);
            this.setOnTouchListener(this);
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        /**
         * 自动缩放任务
         * 
         * @author pengli.cai
         * 
         */
        private class AutoScaleRunnable implements Runnable {
            static final float BIGGER = 1.07f;
            static final float SMALLER = 0.93f;
            private float mTargetScale;
            private float tempScale;

            /**
             * 缩放中心
             */
            private float x;
            private float y;

            /**
             * 传入目标缩放值，根据目标值于当前值，判断应该放大还是缩小
             * 
             * @param targetScale
             *            缩放的比例
             * @param x
             *            缩放的中心x
             * @param y
             *            缩放的中心y
             */
            public AutoScaleRunnable(float targetScale, float x, float y) {
                super();
                this.mTargetScale = targetScale;
                this.x = x;
                this.y = y;
                if (getScale() < targetScale) {// 目标比当前大
                    tempScale = BIGGER;// 放大s
                } else {
                    tempScale = SMALLER;// 缩小
                }
            }

            @Override
            public void run() {
                // 进行具体的缩放
                mScaleMatrix.postScale(tempScale, tempScale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                final float currentScale = getScale();
                // 如果值在合法范围内，继续缩放
                if ((tempScale > 1f && currentScale < mTargetScale)// 放大
                        || (tempScale < 1f && mTargetScale < currentScale)// 缩小
                ) {
                    MyImageView.this.postDelayed(this, AUTO_SCALE_TIME_GAP);
                } else {
                    boolean toInitScale = false;
                    if (mCurrRotateAngle == 90 || mCurrRotateAngle == 270) {
                        if (mImageView.hInitScale == mTargetScale) {
                            toInitScale = true;
                        }
                    } else {
                        if (mImageView.initScale == mTargetScale) {
                            toInitScale = true;
                        }
                    }
                    if (toInitScale) {
                        RectF rectF = mImageView.getMartixRectF();
                        float deltaX = getWidth() / 2 - rectF.centerX();
                        float deltaY = getHeight() / 2 - rectF.centerY();
                        mScaleMatrix.postTranslate(deltaX, deltaY);

                        final float deltaScale = mTargetScale / currentScale;// 设置为目标的缩放比例
                        mScaleMatrix.postScale(deltaScale, deltaScale,
                                ImageTagLayout.this.getWidth() / 2,
                                ImageTagLayout.this.getHeight() / 2);
                        setImageMatrix(mScaleMatrix);
                        isAutoScale = false;
                    } else {
                        final float deltaScale = mTargetScale / currentScale;// 设置为目标的缩放比例
                        mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                        setImageMatrix(mScaleMatrix);
                        isAutoScale = false;
                    }
                }
                if (mytouchListener != null)
                    mytouchListener.myScallChange(x, y);// 刷新下
            }

        }

        /**
         * 这个是缩放
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float tInitScale = initScale;
            if (mCurrRotateAngle == 90 || mCurrRotateAngle == 270) {
                tInitScale = hInitScale;
            }
            float scale = getScale();
            float scaleFactor = detector.getScaleFactor();// 当前的伸缩比
            if (scale <= 0) {
                scale = tInitScale;
            }

            if ((scale < SCALE_MAX * tInitScale && scaleFactor > 1.0f)
                    || (scale > tInitScale && scaleFactor < 1.0f)) {
                /**
                 * 判断最大值和最小值
                 */
                if (scaleFactor * scale < tInitScale) {
                    scaleFactor = tInitScale / scale;
                }
                if (scaleFactor * scale > SCALE_MAX * tInitScale) {
                    scaleFactor = SCALE_MAX * tInitScale / scale;
                }
                /**
                 * 设置缩放比例
                 */

                // 其实代表着是图片没有旋转了
                mScaleMatrix.postScale(scaleFactor, scaleFactor, oriScaleX,
                        oriScaleY);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
            }

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            oldScale = getScale();
            // 决定把拓展中心定死
            oriScaleX = detector.getFocusX();
            oriScaleY = detector.getFocusY();
            return true;
        }

        private float oriScaleX = 0;
        private float oriScaleY = 0;

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            scaleChangeRate = getScale() / oldScale;
            if (mytouchListener != null)
                mytouchListener.myScallChange(oriScaleX, oriScaleY);
        }

        private MyTouchListener mytouchListener = null;

        public MyTouchListener getMytouchListener() {
            return mytouchListener;
        }

        public void setMytouchListener(MyTouchListener mytouchListener) {
            this.mytouchListener = mytouchListener;
        }

        boolean isMove = false;// 这个是用来看看有没有动过 如果动过的话就不添加marker

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mBitmap == null || mTextMoving) {
                return false;
            }
            mScaleGestureDetector.onTouchEvent(event);// 这句和下面一句换位置可以
                                                      // 作为是否响应旋转后的缩放 这句在前为响应
            if (mGestureDetector.onTouchEvent(event))
                return true;

            float x = 0, y = 0;
            final int pointCount = event.getPointerCount();
            for (int i = 0; i < pointCount; i++) {
                x += event.getX(i);
                y += event.getY(i);
            }
            x /= pointCount;
            y /= pointCount;
            /**
             * 当每次触摸点变化时
             */
            if (pointCount != lastPointerCount) {
                mLastX = x;
                mLastY = y;
            }
            lastPointerCount = pointCount;
            RectF rectF = getMartixRectF();
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (true) {
                    if (getDrawable() != null) {
                        isCheckTopAndBottom = true;
                        isCheckLeftAndRight = true;
                        if (rectF.width() < getWidth()) {// 如果宽度小于屏幕宽度，则禁止左右移动
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        if (rectF.height() < getHeight()) {// 小于屏幕高度，禁止上下移动
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkMatrixBounds();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                if (mytouchListener != null)
                    mytouchListener.theMove(event, dx, dy);
                break;
            case MotionEvent.ACTION_UP:
                lastPointerCount = 0;
                isMove = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                isMove = false;
                break;
            }

            return true;
        }

        /**
         * 移动的时候判断边界，主要是判断宽或高大于屏幕
         */
        private void checkMatrixBounds() {
            RectF rectF = getMartixRectF();
            float deltaX = 0, deltaY = 0;
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            // 判断移动或缩放后，图片呢显示是否超出屏幕边界
            if (rectF.top > 0 && isCheckTopAndBottom) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < viewHeight && isCheckTopAndBottom) {
                deltaY = viewHeight - rectF.bottom;
            }
            if (rectF.left > 0 && isCheckLeftAndRight) {
                deltaX = -rectF.left;
            }
            if (rectF.right < viewWidth && isCheckLeftAndRight) {
                deltaX = viewWidth - rectF.right;
            }
            mScaleMatrix.postTranslate(deltaX, deltaY);
        }

        /**
         * 获得当前的缩放比例
         * 
         * @return
         */
        public final float getScale() {
            float tInitScale = initScale;
            if (mCurrRotateAngle == 90 || mCurrRotateAngle == 270) {
                tInitScale = hInitScale;
            }
            float theNewScale = tInitScale;
            if (mCurrRotateAngle == 0 || mCurrRotateAngle == 180) {
                theNewScale = getMartixRectF().width()
                    / getDrawable().getIntrinsicWidth();
            } else {
                theNewScale = getMartixRectF().height()
                        / getDrawable().getIntrinsicWidth();
            }
            return theNewScale;
        }

        private float oldScale = 1;
        private float scaleChangeRate = 1;

        /**
         * 这个用来记录变化的Scale
         */
        public float getChangeScaleRate() {
            return scaleChangeRate;
        }

        /**
         * 在缩放是，进行图片显示范围的控制
         */
        private void checkBorderAndCenterWhenScale() {
            RectF rectF = getMartixRectF();
            float deltaX = 0;
            float deltaY = 0;

            int width = getWidth();
            int height = getHeight();

            // 如果宽或高大于屏幕，就控制范围
            if (rectF.width() >= 0) {
                if (rectF.left > 0) {
                    deltaX = -rectF.left;
                }
                if (rectF.right < width) {
                    deltaX = width - rectF.right;
                }
            }
            if (rectF.height() >= height) {
                if (rectF.top > 0) {
                    deltaY = -rectF.top;
                }
                if (rectF.bottom < height) {
                    deltaY = height - rectF.bottom;
                }
            }

            // 如果宽高小于屏幕，就居中 下面建议用纸画画就知道强行居中了
            if (rectF.width() < width) {
                deltaX = width * 0.5f - rectF.right + 0.5f * rectF.width();
            }
            if (rectF.height() < height) {
                deltaY = height * 0.5f - rectF.bottom + 0.5f * rectF.height();
            }
            mScaleMatrix.postTranslate(deltaX, deltaY);
        }

        /**
         * 根据图片的范围构造一个长方形
         */
        public RectF getMartixRectF() {
            Matrix matrix = mScaleMatrix;
            RectF rectF = new RectF();
            Drawable d = getDrawable();
            if (d != null) {
                // 从0 0 位顶点 然后再设定对角点这样就确定了一个矩形
                rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());// 根据图片获得图片范围
                matrix.mapRect(rectF);
            }
            return rectF;
        }

        private Bitmap mBitmap;
        private boolean globalLayoutCalled;

        private void initLoadedBitmap() {
            // 图片的实际宽高
            int width = getWidth();
            int height = getHeight();
            int dw = mBitmap.getWidth();
            int dh = mBitmap.getHeight();
            float scale = 1.0f;
            // 如果图片的宽或高大于屏幕，则缩放至屏幕宽或者高
            if (dw > width && dh <= height) {
                scale = width * 1.0f / dw;
            } else if (dh > height && dw <= width) {
                scale = height * 1.0f / dh;
            } else if (dh > height && dw > width) {// 宽高都大
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            } else {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            initScale = scale;
            //图片由垂直旋转为水平或水平旋转为垂直时，计算初始缩放比例
            if (dh > width && dw <= height) {
                hInitScale = width * 1.0f / dh;
            } else if (dw > height && dh <= width) {
                hInitScale = height * 1.0f / dw;
            } else if (dw > height && dh > width) {// 宽高都大
                hInitScale = Math.min(width * 1.0f / dh, height * 1.0f / dw);
            } else {
                hInitScale = Math.min(width * 1.0f / dh, height * 1.0f / dw);
            }
            mScaleMatrix.postTranslate((getWidth() - dw) / 2,
                    (getHeight() - dh) / 2);// 由 0 0 开始 必须放在scale前面不知道为啥
                                            // 难道矩阵运算还不是一次的？
            mScaleMatrix.postScale(scale, scale, getWidth() / 2,
                    getHeight() / 2);
            setImageMatrix(mScaleMatrix);

            // add touch listener
            initTheEvent();

            int tmpRotate = mCurrRotateAngle;
            mCurrRotateAngle = 0;
            for (int i = 0; i < tmpRotate / 90; i++) {
                rotateImage();
            }

            // add tags view
            loadTags(tagViewList);
        }

        @Override
        public void onGlobalLayout() {
            globalLayoutCalled = true;
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
            if (mBitmap != null) {
                initLoadedBitmap();
            }
        }

        @Override
        public void setImageBitmap(Bitmap bm) {// 重写是因为imageloader用的就是设定这个方法来重新设置bitmap
            super.setImageBitmap(bm);
            mBitmap = bm;
            if (mBitmap != null && globalLayoutCalled) {
                initLoadedBitmap();
            }
        }

        public Matrix getMyMartix() {
            return mScaleMatrix;
        }
    }

    public void removeLayoutListener() {
        mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(mImageView);
    }

    private interface MyTouchListener {
        /**
         * 伸缩变化
         * 
         * @param oriX
         *            缩放中心X
         * @param oriY
         *            缩放中心Y
         */
        void myScallChange (float oriX, float oriY);

        /**
         * 
         * @param event
         * @param dx
         *            移动的x偏移量
         * @param dy
         *            移动的y偏移量
         */
        void theMove (MotionEvent event, float dx, float dy);
    }
    
    public TagView getSeletedTag() {
        return mCurrSelectedTag;
    }
    
    public void removeLastTag() {
        if (tagViewList.size() > 0) {
            TagView tag = tagViewList.remove(tagViewList.size() - 1);
            removeView(tag);
            for (TagView t : tagViewList) {
                t.setVisibility(View.VISIBLE);
            }
        }
    }
    
    public void removeSelectedTag() {
        if (mCurrSelectedTag != null) {
            tagViewList.remove(mCurrSelectedTag);
            removeView(mCurrSelectedTag);
            mCurrSelectedTag = null;
        }
    }
    
    public boolean cancelTagSelected() {
        if (mCurrSelectedTag != null) {
            mCurrSelectedTag.setSelected(false);
            mCurrSelectedTag = null;
            if (mCallback != null) {
                mCallback.onTagSelected(null);
            }
            return true;
        }
        return false;
    }
    
    public void changeSelectedTag(TagView newTag) {
        if (mCurrSelectedTag == null) {
            mCurrSelectedTag = newTag;
            mCurrSelectedTag.setSelected(true);
        } else {
            if (mCurrSelectedTag != newTag) {
                mCurrSelectedTag.setSelected(false);
                mCurrSelectedTag = newTag;
                mCurrSelectedTag.setSelected(true);
            }
        }
        if (mCallback != null) {
            mCallback.onTagSelected(newTag);
        }
    }
    
    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    
    private Callback mCallback;
    
    public interface Callback {
        void onImageClick (MotionEvent e);
        void onTagSelected (TagView tag);
        void onTagMoved ();
    }
}
