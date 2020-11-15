package com.yq.imageviewer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yq.imageviewer.R;
import com.yq.imageviewer.utils.FileUtil;
import com.yq.imageviewer.utils.StatusBarUtil;
import com.yq.imageviewer.utils.des.ImageUtil;
import com.yq.imageviewer.view.MovieView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;

public class GalleryActivity extends BaseActivity {

    private static final String KEY_DIR = "dir";
    private static final String KEY_INDEX = "index";

    @BindView(R.id.act_gallery_vp) ViewPager mViewPager;

    private List<File> mFileList;

    public static void start(Context context, File dir, int index) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(KEY_DIR, dir);
        intent.putExtra(KEY_INDEX, index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        File dir = (File) getIntent().getSerializableExtra(KEY_DIR);
        int index = getIntent().getIntExtra(KEY_INDEX, 0);

        mFileList = Arrays.asList(dir.listFiles());
        mViewPager.setAdapter(new MAdapter());
        mViewPager.setCurrentItem(index);
    }

    @Override
    public boolean isSwipeBackEnable() {
        return false;
    }

    private class MAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mFileList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            File file = mFileList.get(position);
            if (ImageUtil.TYPE_GIF.equals(ImageUtil.getPicType(file))) {
                MovieView movieView = new MovieView(GalleryActivity.this);
                movieView.setMovieFile(mFileList.get(position).getAbsolutePath());
                container.addView(movieView);
                return movieView;
            } else {
                final PhotoDraweeView iv = new PhotoDraweeView(GalleryActivity.this);
                iv.setController(Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.fromFile(mFileList.get(position)))//如果要用controler，uri必须在这里设置而不能在外面直接image.setImageURI
                    .setOldController(iv.getController())
                    .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                return;
                            }
                            iv.update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                    }).build());
                container.addView(iv);
                return iv;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PhotoDraweeView piv = (PhotoDraweeView) object;
            container.removeView(piv);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }
    }
}
