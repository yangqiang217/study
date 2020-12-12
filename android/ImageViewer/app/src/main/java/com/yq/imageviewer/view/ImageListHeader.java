package com.yq.imageviewer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yq.imageviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangqiang on 11/02/2018.
 */

public class ImageListHeader extends FrameLayout {

    @BindView(R.id.header_imagelist_title) TextView mTvTitle;
    @BindView(R.id.header_imagelist_date) TextView mTvDate;

    private Context mContext;

    public ImageListHeader(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(mContext, R.layout.header_imagelist, this);
        ButterKnife.bind(this);
    }

    public ImageListHeader setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
        return this;
    }

    public ImageListHeader setDate(String date) {
        if (mTvDate != null) {
            mTvDate.setText(date);
        }
        return this;
    }
}
