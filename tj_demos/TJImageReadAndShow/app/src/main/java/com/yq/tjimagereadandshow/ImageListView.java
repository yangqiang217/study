
package com.yq.tjimagereadandshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

public class ImageListView extends LinearLayout implements View.OnClickListener {
    public static final int DEFAULT_IMAGE_SIZE = 65;
    
    private List<ImageViewWrapper> imageViewList = new LinkedList<ImageViewWrapper>();
    private List<Bitmap> bitmapList = new LinkedList<Bitmap>();
    private float density;
    private static final int DEFALUT_MAX_COUNT = 2;
    private int maxCount = DEFALUT_MAX_COUNT;
    private int defaultDrawableId;
    private int addAgainDrawableId = R.drawable.btn_next_pic_selector;
    private int imgViewSize;
    private int margin;
    private OnImageClickListener listener;
    private int currClickIndex = -1;
    private boolean dataModified;
    private boolean canEdited = true;

    public ImageListView (Context context) {
        super(context);
        init();
    }

    public ImageListView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        density = getResources().getDisplayMetrics().density;
        imgViewSize = (int) (DEFAULT_IMAGE_SIZE * density);
        margin = (int) (12 * density);
        ImageViewWrapper iv = new ImageViewWrapper(getContext(), 0);
        iv.setOnClickListener(this);
        int addfirstDrawableId = R.drawable.btn_first_photo_selector;
        iv.setBackgroundResource(addfirstDrawableId);
        LayoutParams layoutParams = new LayoutParams(imgViewSize, imgViewSize);
        addView(iv, layoutParams);
        imageViewList.add(iv);
    }
    
    public void setImageViewSizeMargin(int size, int margin) {
        if (size != imgViewSize || this.margin != margin) {
            imgViewSize = (int) (size * density);
            this.margin = (int) (margin * density);
            for (ImageViewWrapper iv : imageViewList) {
                LayoutParams layoutParams = (LayoutParams) iv.getLayoutParams();
                layoutParams.width = imgViewSize;
                layoutParams.height = imgViewSize;
                if (layoutParams.leftMargin != 0) {
                    layoutParams.leftMargin = margin;
                }
                iv.setLayoutParams(layoutParams);
            }
        }
    }

    public ImageListView setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public ImageListView setDefaultDrawableId(int defaultDrawableId) {
        this.defaultDrawableId = defaultDrawableId;
        imageViewList.get(0).setBackgroundResource(defaultDrawableId);
        return this;
    }
    
    public ImageListView setAddAgainDrawableId(int againDrawableId) {
        addAgainDrawableId = againDrawableId;
        return this;
    }

    public ImageListView setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
        return this;
    }

    public ImageListView setBitmap(int index, Bitmap bmp) {
        if (!canEdited) {
            if (index < maxCount && index > 0) {
                int currSize = imageViewList.size();
                for (int i = 0; i <= index - currSize; i++) {
                    ImageViewWrapper iv = new ImageViewWrapper(getContext(), imageViewList.size());
                    iv.setOnClickListener(this);
                    iv.setBackgroundResource(addAgainDrawableId);
                    LayoutParams layoutParams = new LayoutParams(imgViewSize, imgViewSize);
                    layoutParams.leftMargin = margin;
                    addView(iv, layoutParams);
                    imageViewList.add(iv);
                }
            }
        }

        if (index < imageViewList.size()) {
            imageViewList.get(index).setImageBitmap(bmp);

            if (index < bitmapList.size()) {
                bitmapList.set(index, bmp);
            } else {
                bitmapList.add(bmp);
            }

            if (imageViewList.size() < maxCount && canEdited) {
                if (bitmapList.size() == imageViewList.size()) {
                    ImageViewWrapper iv = new ImageViewWrapper(getContext(), bitmapList.size());
                    iv.setOnClickListener(this);
                    iv.setBackgroundResource(addAgainDrawableId);
                    LayoutParams layoutParams = new LayoutParams(imgViewSize, imgViewSize);
                    layoutParams.leftMargin = margin;
                    addView(iv, layoutParams);
                    imageViewList.add(iv);
                }
            }
        }
        return this;
    }

    public ImageListView clearBitmap(int index) {
        if (index < bitmapList.size()) {
            if (bitmapList.size() == maxCount && index == maxCount - 1) {
                imageViewList.get(index).setImageBitmap(null);
            } else {
                ImageViewWrapper iv = null;
                for (int i = index + 1; i < imageViewList.size(); i++) {
                    iv = imageViewList.get(i);
                    iv.index--;
                }
                imageViewList.remove(index);
                removeViewAt(index);
                LayoutParams lp = (LayoutParams) imageViewList.get(0).getLayoutParams();
                lp.leftMargin = 0;
                imageViewList.get(0).setLayoutParams(lp);
            }
            bitmapList.remove(index);

            if (imageViewList.size() == 1 && bitmapList.size() == 0) {
                if (defaultDrawableId != 0) {
                    imageViewList.get(0).setBackgroundResource(defaultDrawableId);
                }
            } else if (imageViewList.size() < maxCount && canEdited) {
                if (bitmapList.size() == imageViewList.size()) {
                    ImageViewWrapper iv = new ImageViewWrapper(getContext(), bitmapList.size());
                    iv.setOnClickListener(this);
                    iv.setBackgroundResource(addAgainDrawableId);
                    LayoutParams layoutParams = new LayoutParams(imgViewSize, imgViewSize);
                    layoutParams.leftMargin = margin;
                    addView(iv, layoutParams);
                    imageViewList.add(iv);
                }
            }
        }
        return this;
    }

    public ImageListView setAllBitmaps(List<Bitmap> bmpList) {
        if (bitmapList.size() > 0) {
            clearAllBitmaps();
        }

        for (int i = 0; i < maxCount && i < bmpList.size(); i++) {
            setBitmap(bitmapList.size(), bmpList.get(i));
        }
        return this;
    }

    public ImageListView clearAllBitmaps() {
        ImageViewWrapper iv = imageViewList.get(0);
        bitmapList.clear();
        imageViewList.clear();
        removeAllViews();

        LayoutParams layoutParams = new LayoutParams(imgViewSize, imgViewSize);
        addView(iv, layoutParams);
        imageViewList.add(iv);

        iv.setImageBitmap(null);
        if (defaultDrawableId != 0) {
            iv.setBackgroundResource(defaultDrawableId);
        }
        return this;
    }

    public int getCurrClickIndex() {
        return currClickIndex;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    private static long lastClickTime;

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastClickTime < 800) { // 防止短时间内（800毫秒）多次点击
            return;
        }
        lastClickTime = System.currentTimeMillis();

        if (listener != null) {
            ImageViewWrapper iv = (ImageViewWrapper) v;
            currClickIndex = iv.index;
            boolean hasBitmap = bitmapList.size() > iv.index;
            listener.onImageClick(iv.index, hasBitmap, this);
        }
    }

    public boolean isDataModified() {
        return dataModified;
    }

    public void setDataModified(boolean dataModified) {
        this.dataModified = dataModified;
    }

    public boolean isCanEdited() {
        return canEdited;
    }

    public ImageListView setCanEdited(boolean canEdited) {
        this.canEdited = canEdited;
        return this;
    }

    private static class ImageViewWrapper extends ImageView {
        private int index;

        public ImageViewWrapper(Context context, int index) {
            super(context);
            setScaleType(ScaleType.CENTER_CROP);
            this.index = index;
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            try {
                super.onDraw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
                setImageBitmap(null);
            }
        }
    }

    public interface OnImageClickListener {
        void onImageClick (int index, boolean hasBitmap, ImageListView imageListView);
    }
}
