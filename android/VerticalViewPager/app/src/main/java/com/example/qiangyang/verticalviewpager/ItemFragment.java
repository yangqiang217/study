package com.example.qiangyang.verticalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qiangyang on 2017/4/26.
 */

public class ItemFragment extends Fragment {

    @BindView(R.id.act_videoplay_playview) protected SimpleDraweeView mExoPlayerView;
    @BindView(R.id.act_videoplay_avatar) protected ImageView mAvatarView;
    @BindView(R.id.act_videoplay_tv_nickname) protected TextView mTvNickName;
    @BindView(R.id.act_videoplay_btn_follow) protected Button mBtnFollow;
    @BindView(R.id.act_videoplay_iv_close) protected ImageView mIvClose;
    @BindView(R.id.act_videoplay_iv_comment) protected ImageView mIvComment;
    @BindView(R.id.act_videoplay_tv_commentcount) protected TextView mTvCommentCount;
    @BindView(R.id.act_videoplay_iv_liked) protected ImageView mIvLiked;
    @BindView(R.id.act_videoplay_tv_likedcount) protected TextView mTvLikedCount;
    @BindView(R.id.act_videoplay_iv_share) protected ImageView mIvShare;
    @BindView(R.id.act_videoplay_btn_addcomment) protected Button mBtnAddComment;

    private Bean bean;

    public static ItemFragment getInstance(Bean bean) {

        ItemFragment fragment = new ItemFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", bean);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        bean = (Bean) (getArguments() != null ? getArguments().getSerializable("data") : 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreateView");

        View view = inflater.inflate(R.layout.layout_item, container, false);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mExoPlayerView.setImageURI(bean.imageUrl);
        mTvNickName.setText(bean.btnText);
        mTvCommentCount.setText(bean.tvText);
        mBtnFollow.setOnClickListener(onClickListener);
    }

//    public void setData(Bean bean) {
//        this.bean = bean;
//        mTvNickName.setText(bean.btnText);
//        mTvCommentCount.setText(bean.tvText);
//    }


    @Override
    public void onDestroyView() {
        System.out.println("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    private View.OnClickListener onClickListener;
    public void setBtnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class Bean implements Serializable {
        public Bean(String btnText, String tvText, String imageUrl) {
            this.btnText = btnText;
            this.tvText = tvText;
            this.imageUrl = imageUrl;
        }

        String btnText;
        String tvText;
        String imageUrl;
    }
}
