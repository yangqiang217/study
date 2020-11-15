package com.yq.tjimagegallery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yq.tjimagegallery.imagetag.SlideImageTagView;
import com.yq.tjimagegallery.imagetag.TagView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 能编辑tag的页面
 */
public class CPGalleyListPreViewNewActivity extends Activity {
    private Context mContext;
    private FrameLayout mTitleLayout;
    private TextView mTitleText;
    private TextView mRightOtherTV;
    private View mTextPoiNameLayout;
    private TextView mTvTagName;
    private View tvAddTagTip;
    private SlideImageTagView mSlideImageView;
    private View mRightImage;
    private View mLeftImage, mRotateImage;
    private boolean isUpdatePoiName = false;
    private ArrayList<ImageItemDataInfo> packDataList;
    private static final int MAX_LAYOUT_COUNT = 3;
    private TextView tvRoadEvent;
    private boolean isNeedLoadData = false;
    private View viewNotEdit;
    private View layoutRotateEdit;

    private LinearLayout mSearchNameEditView;
    private EditText mSearchEdit;
    private Button mBtnConfirm;

    float dp;
    int heightPixels;
    private Boolean isEdit = false;//isEdit是修改名字

    private int mCurrentImage;
    private int mSeleteType;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        ImageLoader.getInstance().clearMemoryCache();
        super.onCreate(savedInstanceState);
        dp = getResources().getDisplayMetrics().density;
        heightPixels = getResources().getDisplayMetrics().heightPixels;

        setContentView(R.layout.gallery_list_preview_new_layout);
        mContext = CPGalleyListPreViewNewActivity.this;

        mCurrentImage = getIntent().getIntExtra("index", 0);
        mSeleteType = getIntent().getIntExtra("type", 0);

        initTitle();
        initView();
        new LoadTask().execute();
    }

    private class LoadTask extends Thread {
        protected void onPostExecute (boolean result) {
            initData();
            ViewClick();
            //            dismissDialog();
        }

        protected void onPreExecute () {
            //            showDialog(getString(R.string.poi_get_nearby_task));
        }

        protected boolean doInBackground () {
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
            if (mCurrentImage >= packDataList.size()) {
                mCurrentImage = 0;
            }
            return true;
        }

        @Override
        public void run () {
            final boolean result = doInBackground();
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run () {
                            onPostExecute(result);
                        }
                    });
        }

        public void execute () {
            onPreExecute();
            start();
        }
    }

    private void initTitle () {
        mTitleLayout = (FrameLayout) this.findViewById(R.id.title_layout);
        mTitleText = (TextView) mTitleLayout.findViewById(R.id.title_mid_layout_text);
        FrameLayout mTitle_left_frame = (FrameLayout) mTitleLayout.findViewById(R.id.title_left_frame);
        mTitle_left_frame.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        onBackPressed();
                    }
                });

        mRightOtherTV = (TextView) mTitleLayout.findViewById(R.id.title_right_textview);
        mRightOtherTV.setVisibility(View.VISIBLE);
        mRightOtherTV.setText(R.string.task_delete);
        mRightOtherTV.setTextColor(
                mContext.getResources().getColor(
                        R.color.gold_color_task_name));
        mRightOtherTV.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        //                showOkToDelDialog();
                    }
                });
    }

    private void initView () {
        mSearchNameEditView = (LinearLayout) findViewById(R.id.poi_name_edit_layout);
        mSearchEdit = (EditText) this.findViewById(R.id.search_text_edit);
        mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
        mBtnConfirm.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        /* 确定是修改还是增加名字 */
                        isUpdatePoiName = true;
                        mSlideImageView.setCurrTagText(mSearchEdit.getText().toString(), false);
                        if (mSlideImageView.getTagCount() > 0) {
                            viewNotEdit.setEnabled(false);
                            viewNotEdit.setSelected(false);
                            packDataList.get(mCurrentImage).mNotEdit = 0;
                        } else {
                            viewNotEdit.setEnabled(true);
                            if (packDataList.get(mCurrentImage).mNotEdit == 0) {
                                viewNotEdit.setSelected(false);
                            } else {
                                viewNotEdit.setSelected(true);
                            }
                        }
                    }
                });

        mSlideImageView = (SlideImageTagView) findViewById(R.id.gallery_preview_image);
        mRightImage = findViewById(R.id.gallery_preview_image_right);
        mLeftImage = findViewById(R.id.gallery_preview_image_left);
        mRotateImage = findViewById(R.id.gallery_preview_image_rotate);
        viewNotEdit = findViewById(R.id.viewNotEdit);
        layoutRotateEdit = findViewById(R.id.layoutRotateEdit);

        tvRoadEvent = (TextView) findViewById(R.id.tvRoadEvent);
        mTextPoiNameLayout = findViewById(R.id.mTextPoiNameLayout);
        mTvTagName = (TextView) mTextPoiNameLayout.findViewById(R.id.edit_text);
        mTextPoiNameLayout.findViewById(R.id.edit_img).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        if (!currImageFileExist()) {
                            return;
                        }

                        isEdit = true;
                        mSearchNameEditView.setVisibility(View.VISIBLE);
                        mSearchEdit.setText(mTvTagName.getText());

                        mSlideImageView.getCurrTag().toDotState();
                        int xy[] = new int[2];
                        mSlideImageView.getCurrTag().getLocationOnScreen(xy);
                        if (xy[1] * 1.0 / heightPixels > 0.5) {
                            setTagEditAutoMargin();
                        }
                        mSlideImageView.hideOtherTags();
                        mRightImage.setVisibility(View.GONE);
                        mLeftImage.setVisibility(View.GONE);
                        mRotateImage.setVisibility(View.GONE);
                        viewNotEdit.setVisibility(View.GONE);
                    }
                });
        mTextPoiNameLayout.findViewById(R.id.edit_delete).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        if (!currImageFileExist()) {
                            return;
                        }

                        setTextNameLayoutVisibility(View.GONE);
                        layoutRotateEdit.setVisibility(View.VISIBLE);
                        mSlideImageView.removeSelectedTag();
                        if (mSlideImageView.getTagCount() > 0) {
                            viewNotEdit.setEnabled(false);
                            viewNotEdit.setSelected(false);
                            packDataList.get(mCurrentImage).mNotEdit = 0;
                        } else {
                            viewNotEdit.setEnabled(true);
                            if (packDataList.get(mCurrentImage).mNotEdit == 0) {
                                viewNotEdit.setSelected(false);
                            } else {
                                viewNotEdit.setSelected(true);
                            }
                        }
                        isUpdatePoiName = true;
                    }
                });
        tvAddTagTip = findViewById(R.id.tvAddTagTip);
    }

    public void initData () {
        if (packDataList.size() == 1) {
            mRightImage.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.GONE);
        }
        if (packDataList.size() > 1 && mCurrentImage == packDataList.size() - 1) {
            mRightImage.setVisibility(View.GONE);
        }
        if (packDataList.size() > 1 && mCurrentImage == 0) {
            mLeftImage.setVisibility(View.GONE);
        }

        if (packDataList.size() <= mCurrentImage) {
            //            showToast("数据加载异常，请重新打开图片列表界面");
            finish();
            return;
        }
        ImageItemDataInfo data = packDataList.get(mCurrentImage);
        showByRoadEvent(data);

        mSlideImageView.setData(
                packDataList, mCurrentImage, new SlideImageTagView.Callback() {

                    @Override
                    public boolean hideTags (int index) {
                        HashSet<String> badSet = new HashSet<String>();
                        if (badSet.contains(packDataList.get(index).mPicTrueId)) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onPageChanged (int nowPage) {
                /* 删除上一张照片编辑照片动态控件，并保存到数据库 */
                        ImageItemDataInfo packData = packDataList.get(mCurrentImage);
                        mSlideImageView.saveCurrData(mCurrentImage, packData);

                        mSlideImageView.cancelTagSelected(mCurrentImage);
                        mCurrentImage = nowPage;

                        showByRoadEvent(packDataList.get(nowPage));

                        if (!currImageFileExist()) {
                            return;
                        }

                        mTitleText.setText(
                                "已拍照片" + "(" + String.valueOf(mCurrentImage + 1) + "/" + packDataList.size() + ")");

                        if (packDataList.size() == 1) {
                            mRightImage.setVisibility(View.GONE);
                            mLeftImage.setVisibility(View.GONE);
                        }

                        if (packDataList.size() > 1 && 0 < nowPage && nowPage < packDataList.size() - 1) {
                            mRightImage.setVisibility(View.VISIBLE);
                            mLeftImage.setVisibility(View.VISIBLE);
                        }

                        if (packDataList.size() > 1 && nowPage == packDataList.size() - 1) {
                            mRightImage.setVisibility(View.GONE);
                            mLeftImage.setVisibility(View.VISIBLE);
                        }

                        if (packDataList.size() > 1 && nowPage == 0) {
                            mLeftImage.setVisibility(View.GONE);
                            mRightImage.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onTagSelected (int index, TagView tag) {
                        if (tag != null) {
                            mTvTagName.setText(tag.getText());
                            setTextNameLayoutVisibility(View.VISIBLE);
                            layoutRotateEdit.setVisibility(View.GONE);
                        } else {
                            setTextNameLayoutVisibility(View.GONE);
                            layoutRotateEdit.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onItemClick (int index, MotionEvent e) {
                        if (isCurrInvalidPic) {
                            return;
                        }
                        if (mSlideImageView.cancelTagSelected(index)) {
                            return;
                        }
                        if (!currImageFileExist()) {
                            return;
                        }

                        if (mSlideImageView.getTagCount() >= MAX_LAYOUT_COUNT) {
                            //                    showCustomToast("一张照片只能添加三个商铺名称");
                            return;
                        }
                        mSlideImageView.addTag(e);
                        if (e.getRawY() / heightPixels > 0.5) {
                            setTagEditAutoMargin();
                        }
                        isEdit = false;/* 将修改的状态改为添加 */
                        mSearchNameEditView.setVisibility(View.VISIBLE);
                        mSearchEdit.setText("");
                        mRightImage.setVisibility(View.GONE);
                        mLeftImage.setVisibility(View.GONE);
                        mRotateImage.setVisibility(View.GONE);
                        viewNotEdit.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTagMoved () {
                        isUpdatePoiName = true;
                    }

                    @Override
                    public void onBitmapMissed (int index) {
                        currMissedBmpIndex = index;
                        if (index == mCurrentImage) {
                            tvAddTagTip.setVisibility(View.GONE);
                            layoutRotateEdit.setVisibility(View.GONE);
                        }
                    }
                });

        mTitleText.setText("编辑照片" + "(" + String.valueOf(mCurrentImage + 1) + "/" + packDataList.size() + ")");
    }

    private void setAddTagTipVisibility (int visibility) {
        if (visibility != tvAddTagTip.getVisibility()) {
            tvAddTagTip.setVisibility(visibility);
            mSlideImageView.notifyDataSetChanged();
        }
    }

    private int currMissedBmpIndex = -1;

    private boolean isCurrInvalidPic;

    private void showByRoadEvent (ImageItemDataInfo data) {
        String str = "";
        isCurrInvalidPic = false;
        if (currMissedBmpIndex >= 0 && currMissedBmpIndex == mCurrentImage) {
            setAddTagTipVisibility(View.GONE);
        } else {
            setAddTagTipVisibility(View.VISIBLE);
            layoutRotateEdit.setVisibility(View.VISIBLE);
            if (data.mTagArray.size() > 0) {
                viewNotEdit.setEnabled(false);
                viewNotEdit.setSelected(false);
                data.mNotEdit = 0;
            } else {
                viewNotEdit.setEnabled(true);
                if (data.mNotEdit == 0) {
                    viewNotEdit.setSelected(false);
                } else {
                    viewNotEdit.setSelected(true);
                }
            }
        }

        tvRoadEvent.setTextColor(mContext.getResources().getColor(R.color.gold_color_task_name));

        if (isCurrInvalidPic) {
            tvRoadEvent.setVisibility(View.VISIBLE);
            tvRoadEvent.setTextColor(mContext.getResources().getColor(R.color.gray_task));
            str = getString(R.string.invalid_pic);
            tvRoadEvent.setText(str);
        } else {
            tvRoadEvent.setVisibility(View.GONE);
        }
    }

    private boolean currImageFileExist () {
        if (TextUtils.isEmpty(packDataList.get(mCurrentImage).mPictruePath) || !new File(
                packDataList.get(mCurrentImage).mPictruePath).exists()) {
            //            showToast(getString(R.string.reward_image_missed_tip));
            //setResult(RESULT_OK);
            isNeedLoadData = true;
            mSlideImageView.notifyDataSetChanged();
            //            return false;
        }

        return true;
    }

    private void ViewClick () {

        mRotateImage.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        mSlideImageView.rotateCurrImage();
                        isUpdatePoiName = true;
                    }
                });

        viewNotEdit.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        if (viewNotEdit.isSelected()) {
                            viewNotEdit.setSelected(false);
                            packDataList.get(mCurrentImage).mNotEdit = 0;
                        } else {
                            viewNotEdit.setSelected(true);
                            packDataList.get(mCurrentImage).mNotEdit = 1;

                            int size = mSlideImageView.getSize();
                            int curr = mSlideImageView.getCurrentItem();

                            if (curr < size - 1) {
                                mSlideImageView.setCurrentItem(curr + 1);
                            }
                        }
                        isUpdatePoiName = true;
                    }
                });

        /* 向左切换照片 */
        mLeftImage.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        int curr = mSlideImageView.getCurrentItem();
                        if (curr > 0) {
                            mSlideImageView.setCurrentItem(curr - 1);
                        }
                    }
                });

        /* 向右切换照片 */
        mRightImage.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        int size = mSlideImageView.getSize();
                        int curr = mSlideImageView.getCurrentItem();

                        if (curr < size - 1) {
                            mSlideImageView.setCurrentItem(curr + 1);
                        }
                    }
                });
    }

    private void setTagEditAutoMargin () {
        View autoMargin = findViewById(R.id.autoMargin);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) autoMargin.getLayoutParams();
        lp.topMargin = (int) -(dp * 190);
        lp.bottomMargin = (int) (dp * 190);
        autoMargin.setLayoutParams(lp);
        setTextNameLayoutVisibility(View.GONE);
    }

    private void hideTagEditText () {
        mSearchNameEditView.setVisibility(View.GONE);
        View autoMargin = findViewById(R.id.autoMargin);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) autoMargin.getLayoutParams();
        if (lp.topMargin != 0 || lp.bottomMargin != 0) {
            lp.topMargin = 0;
            lp.bottomMargin = 0;
            autoMargin.setLayoutParams(lp);
        }

        if (isEdit) {
            setTextNameLayoutVisibility(View.VISIBLE);
        }
    }

    private void setTextNameLayoutVisibility (int visibility) {
        mTextPoiNameLayout.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            mRightOtherTV.setVisibility(View.GONE);
        } else {
            mRightOtherTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed () {
        /* 返回按钮判断有没有进行编辑操作 ,如果有的话，就要把编辑的名字插入到数据库*/
        if (isUpdatePoiName) {
            if (mCurrentImage >= 0 && mCurrentImage < packDataList.size()) {
                ImageItemDataInfo packData = packDataList.get(mCurrentImage);
                mSlideImageView.saveCurrData(mCurrentImage, packData);
//                Pack_TaskDataManager.getInstance().updatePackTaskData(packData);
            }

            //setResult(RESULT_OK);
            isNeedLoadData = true;
//            showCustomToast("图片所有修改已保存");

        }
        setData();
        super.onBackPressed();
    }


    private void setData () {
        Intent intent = new Intent();
        intent.putExtra("isNeedLoadData", isNeedLoadData);
        intent.putExtra("currentNum", mCurrentImage);
        setResult(Activity.RESULT_OK, intent);
    }

    private void showArrowRotateBtn () {
        if (packDataList.size() <= 1) {
            mRightImage.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.GONE);
        } else {
            if (mCurrentImage == packDataList.size() - 1) {
                mRightImage.setVisibility(View.GONE);
                mLeftImage.setVisibility(View.VISIBLE);
            } else if (mCurrentImage == 0) {
                mLeftImage.setVisibility(View.GONE);
                mRightImage.setVisibility(View.VISIBLE);
            } else {
                mLeftImage.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.VISIBLE);
            }
        }

        mRotateImage.setVisibility(View.VISIBLE);
        viewNotEdit.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
