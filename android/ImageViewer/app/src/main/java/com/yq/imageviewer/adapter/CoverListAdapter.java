package com.yq.imageviewer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yq.imageviewer.Const;
import com.yq.imageviewer.MyApplication;
import com.yq.imageviewer.R;
import com.yq.imageviewer.bean.CoverItem;
import com.yq.imageviewer.event.LastSelectUnCheckEvent;
import com.yq.imageviewer.fragment.HomeFragment;
import com.yq.imageviewer.utils.DeviceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class CoverListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CoverItem> mCoverItems = new ArrayList<>();
    private ElementListener mElementListener;
    private int mColumnWidth;

    private HashMap<MHolder, CoverItem> mSelectedMap = new HashMap<>();

    public CoverListAdapter(Context context) {
        mContext = context;
        mColumnWidth = DeviceUtils.getScreenWidth(MyApplication.getAppContext()) / Const.COLUMN;
    }

    public void setCoverItems(List<CoverItem> coverItems) {
        mCoverItems.clear();
        if (coverItems != null) {
            mCoverItems.addAll(coverItems);
        }
    }

    public void remove(CoverItem coverItem) {
        int pos = -1;
        for (int i = 0; i < mCoverItems.size(); i++) {
            CoverItem item = mCoverItems.get(i);
            if (item == coverItem) {
                pos = i;
                break;
            }
        }
        if (pos >= 0) {
            remove(pos);
        }
    }

    public void remove(int pos) {
        mCoverItems.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(mContext)
            .inflate(R.layout.item_cover, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final MHolder holder = (MHolder) h;

        final CoverItem coverItem = mCoverItems.get(position);
        holder.iv.setController(Fresco.newDraweeControllerBuilder()
            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(coverItem.getCoverFile()))
                .
                    setResizeOptions(new ResizeOptions(mColumnWidth,
                        mColumnWidth * coverItem.getImgOriginalHeight() / coverItem.getImgOriginalWidth()))
                .build())
            .setOldController(holder.iv.getController())
            .setAutoPlayAnimations(true)
            .build());
        holder.iv.setAspectRatio(coverItem.getRatio());
        holder.tvTitle.setText(coverItem.getTitle());
        holder.tvDate.setText(coverItem.getPublishDate());
        holder.tvCount.setText(coverItem.getDirectory().list().length + "张");

        holder.ckSelectMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (HomeFragment.sSelectMode) {
                    select(isChecked, holder, coverItem);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.sSelectMode) {
                    select(!holder.ckSelectMode.isChecked(), holder, coverItem);
                    return;
                }

                if (mElementListener != null) {
                    mElementListener.onClick(coverItem);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HomeFragment.sSelectMode = true;
                select(true, holder, coverItem);

                if (mElementListener != null) {
                    mElementListener.onLongClick(coverItem, holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    private void select(boolean select, MHolder holder, CoverItem coverItem) {
        if (select) {
            holder.enterSelectMode();
            mSelectedMap.put(holder, coverItem);
        } else {
            holder.exitSelectMode();
            mSelectedMap.remove(holder);

            if (mSelectedMap.size() == 0) {
                HomeFragment.sSelectMode = false;
                EventBus.getDefault().post(new LastSelectUnCheckEvent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCoverItems.size();
    }

    public void unSelectAll() {
        for (Map.Entry<MHolder, CoverItem> entry : mSelectedMap.entrySet()) {
            MHolder holder = entry.getKey();
            holder.exitSelectMode();
        }
        mSelectedMap.clear();
    }

    public List<CoverItem> getSelectedItems() {
        List<CoverItem> res = new ArrayList<>(mSelectedMap.size());
        for (Map.Entry<MHolder, CoverItem> entry : mSelectedMap.entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }

    public void setElementListener(ElementListener elementListener) {
        mElementListener = elementListener;
    }

    public static class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_cover_container)
        RelativeLayout contaienr;
        @BindView(R.id.item_cover_image)
        SimpleDraweeView iv;
        @BindView(R.id.item_cover_title)
        TextView tvTitle;
        @BindView(R.id.item_cover_date)
        TextView tvDate;
        @BindView(R.id.item_cover_count)
        TextView tvCount;

        @BindView(R.id.item_cover_select_mode_container)
        RelativeLayout rlSelectModeContainer;
        @BindView(R.id.item_cover_select_mode_ck)
        CheckBox ckSelectMode;

        private boolean mSelectMode = false;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void enterSelectMode() {
            if (mSelectMode) {
                return;
            }
            mSelectMode = true;

            RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) rlSelectModeContainer.getLayoutParams();
            lp.height = contaienr.getHeight();
            rlSelectModeContainer.setLayoutParams(lp);
            rlSelectModeContainer.setVisibility(View.VISIBLE);
            ckSelectMode.setChecked(true);
        }

        public void exitSelectMode() {
            if (!mSelectMode) {
                return;
            }
            mSelectMode = false;
            rlSelectModeContainer.setVisibility(View.GONE);
            ckSelectMode.setChecked(false);
        }
    }

    public interface ElementListener {
        void onClick(CoverItem coverItem);

        void onLongClick(CoverItem coverItem, int pos);
    }
}
