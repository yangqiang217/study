package com.yq.tjimagegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yq.tjimagegallery.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SlideImageView extends HackyViewPager {
    private ImagePagerAdapter mAdapter;
    private Callback mCallback;
    private List<String> mPathList;
    private int mEmptyImageId;

    public SlideImageView (Context context) {
        super(context);
    }

    public SlideImageView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    //初次显示时，如果调用了VieaPager的setCurrrentItem，第0、1个item有可能不需要初始化View
    private List<Integer> mInitNoUseList = new ArrayList<Integer>();

    /**
     * 显示图片预览控件
     * @param pathList 所有图片的路径
     * @param currItem 第一次打开展示第几张，从0开始计
     * @param callback 回调接口
     */
    public void setData(List<String> pathList, int currItem, Callback callback) {
        setData(pathList, currItem, callback, false);
    }
    
    public void setData(List<String> pathList, int currItem, Callback callback, boolean isNetImage) {
        mPathList = pathList;
        mAdapter = new ImagePagerAdapter(pathList, isNetImage);

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

        setAdapter(mAdapter);
        setCurrentItem(currItem);
        mCallback = callback;
        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int nowPage) {
                for (final AdapterItem item : mAdapter.items) {
                    if (item.index != nowPage) {
                        item.mAttacher.update();
                    }
                }

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
     * 顺时针旋转当前预览图片多少度
     * @param degree 度数
     */
    public void rotateCurrImage(float degree) {
        for (final AdapterItem item : mAdapter.items) {
            if (item.index == getCurrentItem()) {
                item.mAttacher.setPhotoViewRotation(-degree);
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

    public void setEmptyImageId(int emptyImageId) {
        this.mEmptyImageId = emptyImageId;
    }

    public interface Callback {
        void onPageChanged (int nowPage);

        void onItemClick (int item);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<String> datas;
        private List<AdapterItem> items = new ArrayList<AdapterItem>();
        private boolean mIsNetImage;
        private DisplayImageOptions mImageLoaderOptions;
        

        public ImagePagerAdapter(List<String> fileList, boolean isNetImage) {
            this.datas = fileList;
            mIsNetImage = isNetImage;
            if (mIsNetImage) {
                mImageLoaderOptions = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(
                                R.drawable.third_photoview_empty_photo)
                        .showImageOnFail(R.drawable.third_photoview_empty_photo)
                        .cacheInMemory(false).cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoaderConfigManager.initImageLoader(getContext().getApplicationContext());
            } else {
                mImageLoaderOptions = new DisplayImageOptions.Builder()
                        .cacheInMemory(false).cacheOnDisk(false)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
            }
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
            ((AdapterItem) object).mAttacher.cleanup();
            items.remove(object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final AdapterItem item = new AdapterItem(getContext(), mIsNetImage);
            item.index = position;
            items.add(item);

            item.mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    if (mCallback != null) {
                        mCallback.onItemClick(position);
                    }
                }
            });

            if (!mInitNoUseList.contains(position)) {
                if (mIsNetImage) {
                    ImageLoader.getInstance().displayImage(datas.get(position), item.mImageView, mImageLoaderOptions,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    item.mProgressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view,
                                                            FailReason failReason) {
                                    String message = null;
                                    switch (failReason.getType()) {
                                        case IO_ERROR:
                                            message = "下载错误";
                                            break;
                                        case DECODING_ERROR:
                                            message = "图片无法显示";
                                            break;
                                        case NETWORK_DENIED:
                                            message = "网络有问题，无法下载";
                                            break;
                                        case OUT_OF_MEMORY:
                                            message = "图片太大无法显示";
                                            break;
                                        case UNKNOWN:
                                            message = "未知的错误";
                                            break;
                                    }
                                    Toast.makeText(getContext(), message,
                                            Toast.LENGTH_SHORT).show();
                                    item.mProgressBar.setVisibility(View.GONE);

                                    item.mAttacher.cleanup();
                                    item.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    item.mProgressBar.setVisibility(View.GONE);
                                    item.mAttacher.update();
                                }
                            });
                } else {
                    Bitmap bmp = ImageLoader.getInstance().loadImageSync(Uri.fromFile(
                            new File(datas.get(position))).toString(), mImageLoaderOptions);
                    if (bmp != null && new File(datas.get(position)).exists()) {
                        item.mImageView.setImageBitmap(bmp);
                    } else {
                        item.mImageView.setImageResource(mEmptyImageId);
                    }
                }
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

    private class AdapterItem extends RelativeLayout {
        private PhotoViewAttacher mAttacher;
        private ImageView mImageView;
        private ProgressBar mProgressBar;
        private int index;

        public AdapterItem(Context context, boolean showLoading) {
            super(context);
            mImageView = new ImageView(context);
            LayoutParams lpImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(mImageView, lpImg);
            if (showLoading) {
                mProgressBar = new ProgressBar(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                addView(mProgressBar, lp);
            }
            mAttacher = new PhotoViewAttacher(mImageView);
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
