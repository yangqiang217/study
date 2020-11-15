package com.yq.imageviewer.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.R;
import com.yq.imageviewer.activity.ImageListActivity;
import com.yq.imageviewer.adapter.CoverListAdapter;
import com.yq.imageviewer.bean.CoverItem;
import com.yq.imageviewer.event.BackPressEvent;
import com.yq.imageviewer.event.ClickDeleteEvent;
import com.yq.imageviewer.event.ClickMergeEvent;
import com.yq.imageviewer.event.EnterSelectModeEvent;
import com.yq.imageviewer.event.LastSelectUnCheckEvent;
import com.yq.imageviewer.event.LoadFinishEvent;
import com.yq.imageviewer.event.MainBottomAnimEvent;
import com.yq.imageviewer.event.RefreshEvent;
import com.yq.imageviewer.utils.AppUtil;
import com.yq.imageviewer.utils.DeviceUtils;
import com.yq.imageviewer.utils.FileUtil;
import com.yq.imageviewer.utils.KeyboardChangeUtil;
import com.yq.imageviewer.view.LoadingImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements CoverListAdapter.ElementListener,
    KeyboardChangeUtil.OnKeyboardChangeListener {

    private static final int THRESHOLD_SCROLL = 200;
    private static final int MAX_WIDTH_SEARCH = 400;
    private static final float ALPHA_SEARCH_FOCUS = 1f;
    private static final float ALPHA_SEARCH_UNFOCUS = 0.3f;

    public static boolean sSelectMode = false;

    @BindView(R.id.frag_home_root)
    RelativeLayout mRootView;
    @BindView(R.id.frag_home_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.frag_home_iv_loading)
    LoadingImageView mIvLoading;
    @BindView(R.id.frag_home_et_filter)
    EditText mEtFilter;
    @BindView(R.id.frag_home_iv_refresh)
    ImageView mIvRefresh;
    @BindView(R.id.frag_home_iv_totop)
    ImageView mIvToTop;
    @BindView(R.id.frag_home_iv_tobottom)
    ImageView mIvToBottom;

    private List<CoverItem> mCoverItemList;
    private CoverListAdapter mAdapter;

    private int mSearchWidth;
    private AnimatorSet mSearchViewAnimSet;
    private ValueAnimator mAlphaAnim, mWidthAnim;

    @Override
    public int viewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault()
            .register(this);

        new KeyboardChangeUtil().addViewChangeListener(mRootView, this);
        mEtFilter.setAlpha(0.5f);
        mIvRefresh.setAlpha(0.7f);
        mIvToTop.setAlpha(0.7f);
        mIvToBottom.setAlpha(0.7f);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(Const.COLUMN,
            StaggeredGridLayoutManager.VERTICAL));
        setItemDecoration(mRecyclerView);

        mAdapter = new CoverListAdapter(getContext());
        mAdapter.setElementListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new MyOnScrollListener());

        mEtFilter.addTextChangedListener(new TextChangeListener());
        mEtFilter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeSearchEtStatus(hasFocus);
            }
        });

        load();
    }

    private void setItemDecoration(RecyclerView recyclerView) {
        VideoListItemDecoration decoration =
            new VideoListItemDecoration(DeviceUtils.dp2px(getContext(), 1));
        recyclerView.addItemDecoration(decoration);
    }

    private void load() {
        mAdapter.setCoverItems(null);
        mAdapter.notifyDataSetChanged();

        mIvLoading.show();
        FileUtil.getCovers(Const.PATH, new FileUtil.GetCoverListener() {
            @Override
            public void onFinish(List<CoverItem> coverItemList) {
                mCoverItemList = coverItemList;
                changeData(mCoverItemList);
                mIvLoading.hide();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                mIvLoading.hide();
            }
        });
    }

    private void changeData(List<CoverItem> coverItemList) {
        mAdapter.setCoverItems(coverItemList);
        mAdapter.notifyDataSetChanged();
        EventBus.getDefault()
            .post(new LoadFinishEvent(coverItemList.size()));
    }

    private void changeSearchEtStatus(final boolean show) {
        if (mEtFilter.getWidth() == 0) {
            return;
        }

        if (mSearchViewAnimSet == null) {
            mSearchWidth = mEtFilter.getWidth();

            mAlphaAnim = ValueAnimator.ofFloat();
            mAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    mEtFilter.setAlpha(val);
                }
            });

            mWidthAnim = ValueAnimator.ofInt();
            mWidthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = (int) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) mEtFilter.getLayoutParams();
                    lp.width = val;
                    mEtFilter.setLayoutParams(lp);
                }
            });

            mSearchViewAnimSet = new AnimatorSet();
            mSearchViewAnimSet.setDuration(200);
            mSearchViewAnimSet.setInterpolator(new LinearInterpolator());
            mSearchViewAnimSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!TextUtils.isEmpty(mEtFilter.getText()) && mEtFilter.getText()
                        .length() != 0) {
                        mEtFilter.setSelection(mEtFilter.getText()
                            .length());
                    }
                }
            });
            mSearchViewAnimSet.playTogether(mAlphaAnim, mWidthAnim);
        }
        if (show) {
            mAlphaAnim.setFloatValues(ALPHA_SEARCH_UNFOCUS, ALPHA_SEARCH_FOCUS);
            mWidthAnim.setIntValues(mEtFilter.getWidth(), MAX_WIDTH_SEARCH);
        } else {
            mAlphaAnim.setFloatValues(ALPHA_SEARCH_FOCUS, ALPHA_SEARCH_UNFOCUS);
            mWidthAnim.setIntValues(mEtFilter.getWidth(), mSearchWidth);
        }
        mSearchViewAnimSet.start();
    }

    private void exitSelectMode() {
        sSelectMode = false;
        mAdapter.unSelectAll();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(RefreshEvent refreshEvent) {
        load();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        //只需要退出selectmode就行
        exitSelectMode();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMergeClickEvent(ClickMergeEvent event) {
        List<CoverItem> selected = mAdapter.getSelectedItems();
        if (selected == null || selected.size() == 0) {
            return;
        }
        final String gap = "*";
        String title = "", date = "";
        final List<File> dirList = new ArrayList<>(selected.size());
        for (int i = 0; i < selected.size(); i++) {
            CoverItem coverItem = selected.get(i);
            dirList.add(coverItem.getDirectory());

            title += coverItem.getTitle();
            date += coverItem.getPublishDate();

            if (i != selected.size() - 1) {
                title += gap;
                date += gap;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View dialogView = View.inflate(getContext(), R.layout.dialog_merge_title, null);

        final EditText etTitle = dialogView.findViewById(R.id.dlg_merge_title);
        final EditText etDate = dialogView.findViewById(R.id.dlg_merge_date);
        final Button button = dialogView.findViewById(R.id.dlg_merge_confirm);

        builder.setView(dialogView);
        builder.setCancelable(true);

        final AlertDialog mergeDialog = builder.create();

        etTitle.setText(title);
        etDate.setText(date);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newTitle = etTitle.getText().toString();
                String newDate = etDate.getText().toString();

                boolean succ = FileUtil.merge(dirList, newTitle + Const.GAP_TITLE_TIME + newDate);
                mergeDialog.dismiss();
                EventBus.getDefault().post(new LastSelectUnCheckEvent());
                exitSelectMode();

                if (succ) {
                    load();
                }
            }
        });
        mergeDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteClickEvent(ClickDeleteEvent event) {
        showDialog("Confirm Delete?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (CoverItem coverItem : mAdapter.getSelectedItems()) {
                    FileUtil.deleteDir(coverItem.getDirectory());
                    mCoverItemList.remove(coverItem);
                    mAdapter.remove(coverItem);
                }
                exitSelectMode();
                EventBus.getDefault().post(new LastSelectUnCheckEvent());
            }
        }, null);
    }

    @OnClick(R.id.frag_home_iv_refresh)
    public void onRefreshClick() {
        load();
    }

    @OnClick(R.id.frag_home_iv_totop)
    public void onToTopClick() {
        mRecyclerView.scrollToPosition(0);
        EventBus.getDefault()
            .post(new MainBottomAnimEvent().setHide(false));
    }

    @OnClick(R.id.frag_home_iv_tobottom)
    public void onToBottomClick() {
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        EventBus.getDefault()
            .post(new MainBottomAnimEvent().setHide(false));
    }

    @Override
    public void onClick(CoverItem coverItem) {
        if (mEtFilter.hasFocus()) {
            AppUtil.hideSoftInput(getActivity());
            return;
        }
        ImageListActivity.start(this, coverItem);
    }

    @Override
    public void onLongClick(final CoverItem coverItem, final int pos) {
        if (mEtFilter.hasFocus()) {
            AppUtil.hideSoftInput(getActivity());
            return;
        }
        EventBus.getDefault().post(new EnterSelectModeEvent());
    }

    @Override
    public void onKeyboardHide() {
        mEtFilter.clearFocus();
    }

    @Override
    public void onKeyboardShow() {
    }

    private static class MyOnScrollListener extends RecyclerView.OnScrollListener {

        private int mScrolledY;
        private boolean mBottomHide;
        private MainBottomAnimEvent mMainBottomAnimEvent;

        MyOnScrollListener() {
            mMainBottomAnimEvent = new MainBottomAnimEvent();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mScrolledY * dy < 0) {
                mScrolledY = 0;
            }
            mScrolledY += dy;
            if (mScrolledY > THRESHOLD_SCROLL && !mBottomHide) {
                EventBus.getDefault()
                    .post(mMainBottomAnimEvent.setHide(true));
                mBottomHide = true;
            } else if (mScrolledY < -THRESHOLD_SCROLL && mBottomHide) {
                EventBus.getDefault()
                    .post(mMainBottomAnimEvent.setHide(false));
                mBottomHide = false;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    private static class VideoListItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        VideoListItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
            outRect.bottom = space;
            outRect.left = space / 2;
            outRect.right = space / 2;
        }
    }

    private class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mCoverItemList == null) {
                return;
            }

            if (TextUtils.isEmpty(s)) {
                changeData(mCoverItemList);
                return;
            }

            List<CoverItem> filteredList = new ArrayList<>();
            for (CoverItem coverItem : mCoverItemList) {
                if (coverItem.getTitle()
                    .contains(s)) {
                    filteredList.add(coverItem);
                }
            }
            changeData(filteredList);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
