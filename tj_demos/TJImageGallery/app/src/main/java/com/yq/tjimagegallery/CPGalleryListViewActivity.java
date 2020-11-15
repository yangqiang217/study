package com.yq.tjimagegallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yq.tjimagegallery.gallerylistview.PopMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 图片列表页
 */
public class CPGalleryListViewActivity extends Activity {
    private ArrayList<ImageItemDataInfo> packDataList;
    private ArrayList<ImageItemDataInfo> currShowList = new ArrayList<ImageItemDataInfo>();
    private ListView mListView;
    private ImageAdapter mAdapter;
    private static final int REQUEST_CODE = 10001;
    /* title相关 */
    private FrameLayout mTitleLayout;
    private TextView mTitleNameText;
    private PopMenu mTypeMenu;
    private String[] mTypeListDefault = new String[]{
            "全部", "未编辑", "已编辑", "不编辑"};
    private String[] mTypeListMissed = new String[]{
            "全部", "未编辑", "已编辑", "不编辑", "图片丢失"};
    private String[] mTypeList = mTypeListDefault;

    private int mSeleteType = 0;
    private TextView mRightOtherTV;

    private RelativeLayout lay_select_all;
    private ImageView img_select_all;
    private TextView select_all_tv;

    private boolean mHasMissedImage;
    private int mSwitchNum;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_list_layout);
        initTitle();
        initView();

        new LoadTask().execute();
    }

    private class LoadTask extends Thread {
        private static final int MAX_FIRST_LOAD = 800;
        private boolean mShowLoading = true;

        public LoadTask () {
        }

        public LoadTask (boolean showLoading) {
            this.mShowLoading = showLoading;
        }

        protected void onPostExecute (boolean result) {
//            if (mShowLoading) {
//                dismissDialog();
//            }
            if (!result) {
                return;
            }
            changeSelectType(mSeleteType);
            mListView.setAdapter(mAdapter);
            mListView.setSelection(mSwitchNum);
            mAdapter.notifyDataSetChanged();

            if (packDataList != null && packDataList.size() >= MAX_FIRST_LOAD) {
                new LoadTask( false).execute();
            }
        }

        protected void onPreExecute () {
//            if (mShowLoading) {
//                showDialog(getString(R.string.poi_get_nearby_task));
//            }
        }

        protected boolean doInBackground () {
            packDataList = TestData.INTANCE.makePicData();
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

    private void initView () {
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new ImageAdapter(this, currShowList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(
                new OnItemClickListener() {

                    @Override
                    public void onItemClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        if (mAdapter.isEditor) {
                            CheckBox cb = (CheckBox) arg1.findViewById(R.id.mCheckBox);
                            cb.setChecked(!cb.isChecked());
                            return;
                        }

                        Intent it = new Intent(CPGalleryListViewActivity.this, CPGalleyListPreViewNewActivity.class);
                        it.putExtra("type", mSeleteType);
                        it.putExtra("index", arg2);
                        startActivityForResult(it, REQUEST_CODE);
                    }
                });
        mListView.setOnItemLongClickListener(
                new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        changeToDelete(!mAdapter.isEditor);
                        return true;
                    }
                });

        lay_select_all = (RelativeLayout) findViewById(R.id.lay_delete_all);
        lay_select_all.findViewById(R.id.btn_delete_all).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        if (mAdapter.getSelectedList().size() == 0) {
                            return;
                        }
                        Toast.makeText(CPGalleryListViewActivity.this, "delete", Toast.LENGTH_SHORT).show();
                    }
                });
        img_select_all = (ImageView) lay_select_all.findViewById(R.id.select_all_iv);
        select_all_tv = (TextView) lay_select_all.findViewById(R.id.select_all_tv);
        lay_select_all.findViewById(R.id.select_all_ray).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        boolean toSelAll = mAdapter.selectCount < mAdapter.getCount();
                        changeSelecteAllView(toSelAll);
                        mAdapter.selectAll(toSelAll);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void changeSelecteAllView (boolean toSelectAll) {
        if (toSelectAll) {
            img_select_all.setImageResource(R.drawable.quanxuan_clicked);
            select_all_tv.setText(R.string.cancle_select_all);
        } else {
            img_select_all.setImageResource(R.drawable.quanxuan_normal);
            select_all_tv.setText(R.string.select_all);
        }
    }

    private void initTitle () {
        mTitleLayout = (FrameLayout) this.findViewById(R.id.title_layout);
        mTitleNameText = (TextView) mTitleLayout.findViewById(R.id.title_mid_layout_text);
        mTitleNameText.setText(mTypeList[0]);
        FrameLayout mTitle_left_frame = (FrameLayout) mTitleLayout.findViewById(R.id.title_left_frame);
        mTitle_left_frame.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        onBackPressed();
                    }
                });

        mTitleNameText.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick (View arg0) {
                        if (mHasMissedImage) {
                            mTypeList = mTypeListMissed;
                            mTypeMenu.setItems(mTypeList);
                        }
                        mTypeMenu.showAsDropDown(mTitleLayout);
                        mTitleNameText.setSelected(true);
                    }
                });

        mRightOtherTV = (TextView) mTitleLayout.findViewById(R.id.title_right_textview);
        mRightOtherTV.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick (View v) {
                        if (mAdapter.getCount() == 0) {
                            return;
                        }
                        changeToDelete(!mAdapter.isEditor);
                    }
                });

        mTypeMenu = new PopMenu(this);
        mTypeMenu.addItems(mTypeList);
        mTypeMenu.setCurrentPosition(0);
        mTypeMenu.setOnDismissListener(
                new OnDismissListener() {

                    @Override
                    public void onDismiss () {
                        mTitleNameText.setSelected(false);
                    }
                });

        mTypeMenu.setOnItemClickListener(
                new OnItemClickListener() {

                    @Override
                    public void onItemClick (AdapterView<?> arg0, View arg1, int position, long arg3) {
                        String title = mTypeList[position];
                        String type = "1";
                        if ("未编辑".equals(title)) {
                            type = "2";
                        } else if ("已编辑".equals(title)) {
                            type = "3";
                        } else if ("图片丢失".equals(title)) {
                            type = "4";
                        } else if ("不编辑".equals(title)) {
                            type = "5";
                        }
                        mTypeMenu.dismiss();
                        mTypeMenu.setCurrentPosition(position);
                        if (mSeleteType == position) {
                            return;
                        }
                        changeSelectType(position);
                        changeToDelete(false);
                    }
                });
    }

    private void changeToDelete (boolean toDelete) {
        if (!toDelete) {
            mRightOtherTV.setText(R.string.task_delete);
            lay_select_all.setVisibility(View.GONE);
        } else {
            mRightOtherTV.setText(R.string.cancel);
            img_select_all.setImageResource(R.drawable.quanxuan_normal);
            select_all_tv.setText(R.string.select_all);
            lay_select_all.setVisibility(View.VISIBLE);
        }
        mAdapter.changeEditState(toDelete);
        mAdapter.notifyDataSetChanged();
    }

    private void changeSelectType (int type) {
        mTitleNameText.setText(mTypeList[type]);
        mSeleteType = type;
        if (packDataList == null) {
            return;
        }
        currShowList.clear();
        for (int i = 0; i < packDataList.size(); i++) {
            ImageItemDataInfo data = packDataList.get(i);
            if (TextUtils.isEmpty(data.mPictruePath) || !new File(data.mPictruePath).exists()) {
                mHasMissedImage = true;
                if (type == 4) {
                    currShowList.add(data);
                    continue;
                }
            }
            if (type == 0) {
                currShowList.add(data);
            } else if (type == 1) {
                if (data.mTagArray.size() == 0 && data.mNotEdit == 0) {
                    currShowList.add(data);
                }
            } else if (type == 2) {
                if (data.mTagArray.size() > 0) {
                    currShowList.add(data);
                }
            } else if (type == 3) {
                if (data.mTagArray.size() == 0 && data.mNotEdit != 0) {
                    currShowList.add(data);
                }
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            mSwitchNum = data.getIntExtra("currentNum", 0);
            if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
                if (data.getBooleanExtra("isNeedLoadData", false)) {
                    new LoadTask( true).execute();
                    setResult(RESULT_OK);
                } else {
                    mListView.setAdapter(mAdapter);
                    mListView.setSelection(mSwitchNum);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<ImageItemDataInfo> mPathList;
        private DisplayImageOptions options;
        private boolean isEditor;
        private HashMap<ImageItemDataInfo, Boolean> selectedMap = new HashMap<ImageItemDataInfo, Boolean>();
        private int selectCount;

        public ImageAdapter (Context context, List<ImageItemDataInfo> pathList) {
            mContext = context;
            mPathList = pathList;
            for (ImageItemDataInfo data : mPathList) {
                selectedMap.put(data, false);
            }
            options = new DisplayImageOptions.Builder().showImageForEmptyUri(
                    R.drawable.image_missed).showImageOnFail(R.drawable.image_missed).delayBeforeLoading(
                    100).cacheInMemory(true).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfigManager.initImageLoader(mContext.getApplicationContext());
        }

        @Override
        public void notifyDataSetChanged () {
            for (ImageItemDataInfo data : mPathList) {
                if (selectedMap.get(data) == null) {
                    selectedMap.put(data, false);
                }
            }
            super.notifyDataSetChanged();
        }

        public void changeEditState (boolean edit) {
            if (isEditor != edit) {
                isEditor = edit;

                if (isEditor) {
                    for (ImageItemDataInfo key : selectedMap.keySet()) {
                        selectedMap.put(key, false);
                    }
                    selectCount = 0;
                }
            }
        }

        public void selectAll (boolean select) {
            for (ImageItemDataInfo key : selectedMap.keySet()) {
                selectedMap.put(key, select);
            }
            if (select) {
                selectCount = mPathList.size();
            } else {
                selectCount = 0;
            }
        }

        public List<ImageItemDataInfo> getSelectedList () {
            List<ImageItemDataInfo> list = new ArrayList<ImageItemDataInfo>();
            for (ImageItemDataInfo data : mPathList) {
                Boolean sel = selectedMap.get(data);
                if (sel != null && sel) {
                    list.add(data);
                }
            }
            return list;
        }

        @Override
        public int getCount () {
            return mPathList == null ? 0 : mPathList.size();
        }

        @Override
        public Object getItem (int position) {
            return mPathList.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_gallery_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ImageItemDataInfo data = mPathList.get(position);

            if (data.mTagArray.size() > 0) {
                if (data.mTagArray.size() > 1) {
                    holder.mTextView.setText(data.mTagArray.get(0).getmName());
                    holder.mTextViewDeng.setVisibility(View.VISIBLE);
                } else {
                    holder.mTextView.setText(data.mTagArray.get(0).getmName());
                    holder.mTextViewDeng.setVisibility(View.GONE);
                }
                holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.light_gray));
            } else {
                holder.mTextViewDeng.setVisibility(View.GONE);
                holder.mTextView.setText("请编辑商铺名");
                holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.gray_task));
            }

            if (data.mNotEdit == 1 && data.mTagArray.size() <= 0) {
                holder.mTextView.setText(R.string.not_edit);
                holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.light_gray));
            }

            holder.mCheckBox.setOnCheckedChangeListener(null);
            if (isEditor) {
                Boolean checked = selectedMap.get(data);
                if (checked == null) {
                    checked = false;
                    selectedMap.put(data, false);
                }
                holder.mCheckBox.setChecked(checked);
                holder.mCheckBox.setVisibility(View.VISIBLE);
            } else {
                holder.mCheckBox.setVisibility(View.GONE);
            }
            holder.mCheckBox.setOnCheckedChangeListener(
                    new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                selectCount++;
                            } else {
                                selectCount--;
                            }
                            selectedMap.put(data, isChecked);
                            notifyDataSetChanged();
                            if (selectCount == getCount() && selectCount != 0) {
                                changeSelecteAllView(true);
                            } else {
                                changeSelecteAllView(false);
                            }
                        }
                    });

            holder.mEditImage.setVisibility(View.VISIBLE);
            if (data.mNotEdit == 1) {
                holder.mEditImage.setBackgroundResource(R.drawable.not_edit_icon);
            } else {
                holder.mEditImage.setBackgroundResource(R.drawable.edit_name_modify);
            }

            holder.mImageView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {
                            // 进入大图幻灯片预览界面
                            Intent intent = new Intent(mContext, CPAreaPicSlidePreviewActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            intent.putExtra("currImage", position);
                            intent.putExtra("type", mSeleteType);
                            startActivityForResult(intent, REQUEST_CODE);
                            //CPSubmitPicPreviewActivity.show(mContext, data.mPictruePath);
                        }
                    });

            ImageLoader.getInstance().displayImage(
                    Uri.fromFile(new File(data.mPictruePath)).toString(), holder.mImageView, options,
                    new ImageLoadingListener() {


                        @Override
                        public void onLoadingStarted (String imageUri, View view) {
                            if (imageUri != null && imageUri.startsWith("file://")) {
                                String path = imageUri.replaceFirst("file://", "");
                                if (!TextUtils.isEmpty(path) && !new File(path).exists()) {
                                    ImageLoader.getInstance().clearMemoryCache();
                                }
                            }
                        }

                        @Override
                        public void onLoadingComplete (String imageUri, View view, Bitmap loadedImage) {

                        }

                        @Override
                        public void onLoadingCancelled (String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed (String imageUri, View view, FailReason failReason) {

                        }
                    });

            return convertView;
        }

        private class ViewHolder {
            private ImageView mImageView;
            private TextView mTextView;
            private TextView mTextViewDeng;
            private ImageView mEditImage;
            private CheckBox mCheckBox;

            public ViewHolder (View root) {
                mImageView = (ImageView) root.findViewById(R.id.mImageView);
                mTextView = (TextView) root.findViewById(R.id.mTextView);
                mTextViewDeng = (TextView) root.findViewById(R.id.mTextViewDeng);
                mEditImage = (ImageView) root.findViewById(R.id.mEditImage);
                mCheckBox = (CheckBox) root.findViewById(R.id.mCheckBox);
            }
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (mTypeMenu != null) {
            mTypeMenu.showSelectBtn(false);
        }
    };

    @Override
    public void onBackPressed () {
        if (mAdapter.isEditor) {
            changeToDelete(false);
            return;
        }
        super.onBackPressed();
    }
}
