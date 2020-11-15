package com.example.yangqiang.recyclerviewdragsort;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelSortAdapter extends RecyclerView.Adapter {

    private List<String> mData;

    public void setData(List<String> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_sort, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String data = mData.get(position);
        MViewHolder viewHolder = (MViewHolder) holder;
        viewHolder.mTv.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.channel_list_container)
        FrameLayout mContainer;
        @BindView(R.id.item_text)
        TextView mTv;

        MViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContainer.getLayoutParams().width =
                ChannelSortViewGroup.sItemSize + ChannelSortViewGroup.sMarginBetweenItem;
            mContainer.getLayoutParams().height =
                ChannelSortViewGroup.sItemSize + ChannelSortViewGroup.sMarginBetweenItem;

            mTv.getLayoutParams().width = ChannelSortViewGroup.sItemSize;
            mTv.getLayoutParams().height = ChannelSortViewGroup.sItemSize;

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContainer.setTranslationX(100);
                    mContainer.setTranslationY(100);
                }
            });
        }
    }
}
