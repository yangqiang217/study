package com.yq.imageviewer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yq.imageviewer.R;
import com.yq.imageviewer.adapter.ImageListAdapter;
import com.yq.imageviewer.bean.CoverItem;
import com.yq.imageviewer.utils.DeviceUtils;
import com.yq.imageviewer.utils.FileUtil;
import com.yq.imageviewer.utils.StatusBarUtil;
import com.yq.imageviewer.utils.des.DesUtil;
import com.yq.imageviewer.view.ImageListDecoration;
import com.yq.imageviewer.view.ImageListHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageListActivity extends BaseActivity implements ImageListAdapter.OnImageClickListener {

    private static final String KEY_DIR = "dir";

    @BindView(R.id.act_imagelist_rv) RecyclerView mRecyclerView;

    private CoverItem mCoverItem;

    public static void start(Fragment fragment, CoverItem coverItem) {
        Intent intent = new Intent(fragment.getContext(), ImageListActivity.class);
        intent.putExtra(KEY_DIR, coverItem);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        mCoverItem = (CoverItem) getIntent().getSerializableExtra(KEY_DIR);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<File> fileList = Arrays.asList(mCoverItem.getDirectory().listFiles());
        ImageListAdapter adapter = new ImageListAdapter(fileList);
        adapter.setOnImageClickListener(this);
        ImageListHeader header = new ImageListHeader(this);
        header.setTitle(mCoverItem.getTitle()).setDate(mCoverItem.getPublishDate());
        adapter.setHeader(header);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new ImageListDecoration(5, Color.TRANSPARENT));
    }

    @Override
    public void onClick(int pos) {
        GalleryActivity.start(this, mCoverItem.getDirectory(), pos);
    }
}
