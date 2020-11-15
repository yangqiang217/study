package com.yq.tjimagegallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 点列表左边的图进入预览大图
 */
public class CPAreaPicSlidePreviewActivity extends Activity {
    private SlideImageView mSlidePreview;
    private List<String> previewPathList;
    private int mCurrentPreViewImage;
    private ArrayList<ImageItemDataInfo> packDataList;
    private View mLeftImage, mRightImage;
    private boolean isNeedLoadData = false;
    private int mCurrentNum;
    private int mSeleteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageLoader.getInstance().clearMemoryCache();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_slide_preview);
        mCurrentPreViewImage = getIntent().getIntExtra("currImage", 0);
        mSeleteType = getIntent().getIntExtra("type", 0);
        new LoadTask().execute();
    }

    private class LoadTask extends Thread {
        protected void onPostExecute(boolean result) {
            initView();
            ViewClick();
//            dismissDialog();
        }

        protected void onPreExecute() {
//            showDialog(getString(R.string.poi_get_nearby_task));
        }

        protected boolean doInBackground() {
            //自己准备数据
            packDataList = TestData.INTANCE.makePicData();

            if (mSeleteType != 0) {
                ArrayList<ImageItemDataInfo> list = new ArrayList<ImageItemDataInfo>();
                for (int i = 0; i < packDataList.size(); i++) {
                    ImageItemDataInfo data = packDataList.get(i);
                    if (mSeleteType == 1) {
                        if (data.mTagArray.size() == 0 && data.mNotEdit == 0) {
                            list.add(data);
                        }
                    } else if (mSeleteType == 2) {
                        if (data.mTagArray.size() > 0) {
                            list.add(data);
                        }
                    } else if (mSeleteType == 3) {
                        if (data.mTagArray.size() == 0 && data.mNotEdit != 0) {
                            list.add(data);
                        }
                    } else if (mSeleteType == 4) {
                        if (TextUtils.isEmpty(data.mPictruePath) || !new File(data.mPictruePath).exists()) {
                            list.add(data);
                        }
                    }
                }
                packDataList.clear();
                packDataList = list;
            }
            
            /*为了避免packDataList从数据库读取失败导致的，列表和index不匹配越界的问题*/
            if (mCurrentPreViewImage >= packDataList.size()) {
                mCurrentPreViewImage = 0;
            }
            return true;
        }

        @Override
        public void run() {
            final boolean result = doInBackground();
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run () {
                            onPostExecute(result);
                        }
                    });
        }

        public void execute() {
            onPreExecute();
            start();
        }
    }

    private void ViewClick() {
    /* 向左切换照片 */
        mLeftImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int curr = mSlidePreview.getCurrentItem();
                if (curr > 0) {
                    mSlidePreview.setCurrentItem(curr - 1);
                }
            }
        });

        /* 向右切换照片 */
        mRightImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int size = mSlidePreview.getSize();
                int curr = mSlidePreview.getCurrentItem();

                if (curr < size - 1) {
                    mSlidePreview.setCurrentItem(curr + 1);
                }
            }
        });


    }

    private void initView() {
        mSlidePreview = (SlideImageView) findViewById(R.id.gallery_preview_big_image);
        mSlidePreview.setEmptyImageId(R.drawable.image_missed);
        mRightImage = findViewById(R.id.gallery_preview_image_right);
        mLeftImage = findViewById(R.id.gallery_preview_image_left);
        previewPathList = new ArrayList<String>();
        for (int i = 0; i < packDataList.size(); i++) {
            String name = packDataList.get(i).mPictruePath;
            previewPathList.add(name);
        }
        isSwitchBtnShow(mCurrentPreViewImage);
        mSlidePreview.setData(previewPathList, mCurrentPreViewImage,
                new SlideImageView.Callback() {

                    @Override
                    public void onPageChanged(int nowPage) {
                        // TODO Auto-generated method stub
                        // mPageText.setText(nowPage+1 + "/" +
                        // packDataList.size());
                        mCurrentNum = nowPage;
                        isSwitchBtnShow(nowPage);
                    }

                    @Override
                    public void onItemClick(int item) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void isSwitchBtnShow(int nowPage) {

        if (packDataList.size() == 1) {
            mRightImage.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.GONE);
        }

        if (packDataList.size() > 1
                && 0 < nowPage && nowPage < packDataList.size() - 1) {
            mRightImage.setVisibility(View.VISIBLE);
            mLeftImage.setVisibility(View.VISIBLE);
        }

        if (packDataList.size() > 1
                && nowPage == packDataList.size() - 1) {
            mRightImage.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.VISIBLE);
        }

        if (packDataList.size() > 1 && nowPage == 0) {
            mLeftImage.setVisibility(View.GONE);
            mRightImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        setData();
        super.onBackPressed();
    }

    private void setData() {
        Intent intent = new Intent();
        intent.putExtra("isNeedLoadData", isNeedLoadData);
        intent.putExtra("currentNum", mCurrentNum);
        setResult(Activity.RESULT_OK, intent);
    }

}
