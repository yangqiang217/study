package com.yq.tjimagegallery.imagetag;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.yq.tjimagegallery.ImageItemDataInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SlideImageTagView extends ViewPager {
    private ImagePagerAdapter mAdapter;
    private Callback mCallback;
    private List<ImageItemDataInfo> mPathList = new ArrayList<ImageItemDataInfo>();

    public SlideImageTagView(Context context) {
        super(context);
    }

    public SlideImageTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    //初次显示时，如果调用了VieaPager的setCurrrentItem，第0、1个item有可能不需要初始化View
    private List<Integer> mInitNoUseList = new ArrayList<Integer>();

    /**
     * 显示图片预览控件
     * @param pathList 所有图片的路径
     * @param currItem 第一次打开展示第几张，从0开始计
     * @param callback 回调接口
     */
    public void setData(List<ImageItemDataInfo> pathList, int currItem, Callback callback) {
        mCallback = callback;

        if (currItem < 0) {
            currItem = 0;
        } else if (currItem >= pathList.size()) {
            currItem = pathList.size() - 1;
        }
        if (pathList.size() > 3 && currItem >= 2) {
            mInitNoUseList.add(0);
            if (currItem > 2) {
                mInitNoUseList.add(1);
            }
        }

        mPathList.addAll(pathList);
        mAdapter = new ImagePagerAdapter(mPathList);
        setAdapter(mAdapter);
        setCurrentItem(currItem);
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int nowPage) {
                if (mCallback != null) {
                    mCallback.onPageChanged(nowPage);
                }
            }

        });
    }

    public int getSize() {
        return mPathList == null ? 0 : mPathList.size();
    }
    
    /**
     * 顺时针旋转当前预览图片
     */
    public void rotateCurrImage() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                item.rotateImage();
                return;
            }
        }
    }

    public void deleteImageFile(int item) {
        try {
            mPathList.remove(item);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addTag(MotionEvent e) {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                item.addTag(e);
                return;
            }
        }
    }
    
    public boolean cancelTagSelected(int index) {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == index) {
                return item.cancelTagSelected();
            }
        }
        return false;
    }
    
    public void removeSelectedTag() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                item.removeSelectedTag();
            }
        }
    }
    
    public TagView setCurrTagText(String text, boolean isCancelEdit) {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                if (item.mCurrTouchTag != null) {
                    TagView tag = item.mCurrTouchTag;
                    if (isCancelEdit) {
                        tag.forceWrapContent(tag.getText().toString());
                    } else {
                        tag.forceWrapContent(text);
                        tag.setText(text);
                    }
                    item.changeSelectedTag(tag);
                    for (TagView t : item.tagViewList) {
                        t.setVisibility(View.VISIBLE);
                    }
                    return tag;
                }
                return null;
            }
        }
        return null;
    }
    
    public TagView getCurrTag() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                return item.mCurrSelectedTag;
            }
        }
        return null;
    }

    public void hideOtherTags() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                for (TagView t : item.tagViewList) {
                    if (t != item.mCurrSelectedTag) {
                        t.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }
    
    public void removeEmptyTag() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                item.removeLastTag();
                return;
            }
        }
    }
    
    public int getTagCount() {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                return item.tagViewList.size();
            }
        }
        return 0;
    }
    
    public void saveCurrData(int index, ImageItemDataInfo info) {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == index) {
                info.mTagArray.clear();
                ImageItemDataInfo.PicTagInfo tagInfo = null;
                TagView tag = null;
                for (int i = 0; i < item.tagViewList.size(); i++) {
                    tag = item.tagViewList.get(i);
                    if (!TextUtils.isEmpty(tag.getText())) {
                        tagInfo = new ImageItemDataInfo.PicTagInfo(tag.mXRate, tag.mYRate, tag.getText().toString());
                        info.mTagArray.add(tagInfo);
                    }
                }
                info.mTagCount = info.mTagArray.size();
                info.mRotate = item.getCurrRotateAngle();
                if (info.mNotEdit == 1 && info.mTagArray.size() > 0) {
                    info.mNotEdit = 0;
                }
            }
        }
    }

    public interface Callback {
        void onPageChanged (int nowPage);
        void onTagSelected (int index, TagView tag);
        void onItemClick (int index, MotionEvent e);
        void onTagMoved ();
        void onBitmapMissed (int index);
        boolean hideTags (int index);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<ImageItemDataInfo> datas;
        private List<AdapterItem> items = new ArrayList<AdapterItem>();
        private DisplayImageOptions options;

        public ImagePagerAdapter(List<ImageItemDataInfo> fileList) {
            this.datas = fileList;
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((AdapterItem) object).removeLayoutListener();
            items.remove(object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final AdapterItem item = new AdapterItem(getContext(), position);
            items.add(item);

            if (!mInitNoUseList.contains(position)) {
                ImageItemDataInfo data = datas.get(position);

                Bitmap bmp = ImageLoader.getInstance().loadImageSync(Uri.fromFile(
                        new File(data.mPictruePath)).toString(), new ImageSize(
                        container.getWidth(), container.getHeight()), options);
                if (bmp == null || !new File(data.mPictruePath).exists()) {
                    bmp = null;
                    if (mCallback != null) {
                        mCallback.onBitmapMissed(position);
                    }
                }
                boolean hideTags = false;
                if (mCallback != null && mCallback.hideTags(position)) {
                    hideTags = true;
                }
                item.loadImageAndTags(bmp, data, hideTags);
            } else {
                mInitNoUseList.remove(Integer.valueOf(position));
            }

            ((ViewPager) container).addView(item);
            return item;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class AdapterItem extends ImageTagLayout {
        private int index;

        public AdapterItem(Context context, int idx) {
            super(context);
            this.index = idx;
            setCallback(new ImageTagLayout.Callback() {

                @Override
                public void onTagSelected(TagView tag) {
                    if (mCallback != null) {
                        mCallback.onTagSelected(index, tag);
                    }
                }

                @Override
                public void onImageClick(MotionEvent e) {
                    if (mCallback != null) {
                        mCallback.onItemClick(index, e);
                    }
                }

                @Override
                public void onTagMoved() {
                    if (mCallback != null) {
                        mCallback.onTagMoved();
                    }
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
