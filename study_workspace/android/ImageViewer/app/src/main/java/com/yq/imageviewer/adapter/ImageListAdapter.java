package com.yq.imageviewer.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yq.imageviewer.MyApplication;
import com.yq.imageviewer.R;
import com.yq.imageviewer.utils.DeviceUtils;
import com.yq.imageviewer.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 瀑布流的adapter
 */
public class ImageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_HEADER = 1;

    private List<File> mImageList = new ArrayList<>();
    private OnImageClickListener mOnImageClickListener;
    private View mHeader;

    private int mScreenWidth;

    public ImageListAdapter(List<File> imageList) {
        mImageList = imageList;
        mScreenWidth = DeviceUtils.getScreenWidth(MyApplication.getAppContext());
    }

    public void setHeader(View header) {
        mHeader = header;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View viewNormal = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_list, parent, false);
            return new ImageHolder(viewNormal);
        } else {
            return new SimpleViewHolder(mHeader);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            final int adjPosition = position - getHeaderNo();
            if (adjPosition < 0 || adjPosition >= mImageList.size()) {
                return;
            }

            ImageHolder imageHolder = (ImageHolder) holder;
            addListener(imageHolder, adjPosition);
            File imageFile = mImageList.get(adjPosition);
            int[] imageSize = FileUtil.getSize(imageFile.getPath());
            imageHolder.image.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(imageFile)).
                    setResizeOptions(new ResizeOptions(mScreenWidth, mScreenWidth * imageSize[1] / imageSize[0])).build())
                .setOldController(imageHolder.image.getController())
                .setAutoPlayAnimations(true)
                .build());

            if (imageSize[1] != 0) {
                imageHolder.image.setAspectRatio(imageSize[0] / (float) imageSize[1]);
            }
        }
    }

    private void addListener(ImageHolder imageHolder, final int adjPosition) {
        //item click listener
        imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onClick(adjPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size() + getHeaderNo();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    private boolean isHeader(int position) {
        return mHeader != null && position == 0;
    }

    private int getHeaderNo() {
        return mHeader == null ? 0 : 1;
    }

    /**
     * 普通视频item的ViewHolder
     */
    public class ImageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_imagelist_image) SimpleDraweeView image;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * loadmore和pullrefresh的holder
     */
    private static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public ImageListAdapter setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
        return this;
    }

    public interface OnImageClickListener {
        void onClick(int pos);
    }
}
