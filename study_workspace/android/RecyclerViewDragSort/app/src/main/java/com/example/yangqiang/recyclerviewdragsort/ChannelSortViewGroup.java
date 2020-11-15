package com.example.yangqiang.recyclerviewdragsort;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

public class ChannelSortViewGroup extends FrameLayout {

    /** 屏幕适配，动态计算 */
    public static int sMarginSide;
    public static int sItemSize;
    public static int sMarginBetweenItem;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.text1)
    TextView mTv1;
    @BindView(R.id.text2)
    TextView mTv2;
    @BindView(R.id.img_back)
    ImageView mIvClose;

    private List<String> mList;
    private ChannelSortAdapter mAdapter;

    public ChannelSortViewGroup(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ChannelSortViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_channel_sort, this);
        ButterKnife.bind(this);

        calculateSizes();
        adjustUISizeAndLocation();

        mAdapter = new ChannelSortAdapter();

        mList = new ArrayList<>();
        fill();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        new ItemTouchHelper(new ItemTouchCallback()).attachToRecyclerView(mRecyclerView);
    }

    private void fill() {
        mList.clear();
        for (int i = 0; i < 21; i++) {
            mList.add(i + "");
        }
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }

    private void calculateSizes() {
        int screenWidth = ViewUtils.getScreenWidth();
        sMarginSide = (int) (screenWidth * (25 / 375f));
        sItemSize = (int) (screenWidth * (70 / 375f));
        sMarginBetweenItem = (int) (screenWidth * (15 / 375f));
    }

    private void adjustUISizeAndLocation() {
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mTv1.getLayoutParams();
        lp1.leftMargin = sMarginSide;

        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) mTv2.getLayoutParams();
        lp2.leftMargin = sMarginSide;

        mIvClose.setPadding(mIvClose.getPaddingLeft(),
            mIvClose.getPaddingTop(),
            sMarginSide,
            mIvClose.getPaddingBottom());

        mRecyclerView.setPadding(sMarginSide - sMarginBetweenItem / 2, 0,
            sMarginSide - sMarginBetweenItem / 2, 0);
        RelativeLayout.LayoutParams lpList = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
        lpList.topMargin = lpList.topMargin - sMarginBetweenItem / 2;
    }

    private class ItemTouchCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            System.out.println("getMovementFlags");
            if (viewHolder.getLayoutPosition() == 0) {
                return 0;
            }
            return makeMovementFlags(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
//            System.out.println("onMove");

            //注意这里有个坑的，itemView 都移动了，对应的数据也要移动
            System.out.println("onmove, current: " + viewHolder.getAdapterPosition() + ", target: " + target.getAdapterPosition());
            itemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
            boolean isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            System.out.println("onSelectedChanged: " + (actionState == ACTION_STATE_DRAG));
            if (actionState == ACTION_STATE_DRAG) {
                //长按时调用
                changeItemSize(true,
                    ((ChannelSortAdapter.MViewHolder) viewHolder).mTv,
                    ((ChannelSortAdapter.MViewHolder) viewHolder).mContainer);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //松手时调用
            changeItemSize(false,
                ((ChannelSortAdapter.MViewHolder) viewHolder).mTv,
                ((ChannelSortAdapter.MViewHolder) viewHolder).mContainer);

            System.out.println("clearView");
            for (String s : mList) {
                System.out.print(s + ", ");
            }
            System.out.println();
        }

        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current,
            RecyclerView.ViewHolder target) {
//            System.out.println("canDropOver");
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            System.out.println("onSwiped, dir: " + direction);
        }

        void itemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        private void changeItemSize(boolean magnify, View tv, View parent) {
            if (tv == null || tv.getLayoutParams() == null || parent == null || parent.getLayoutParams() == null) {
                return;
            }
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tv.getLayoutParams();
            if (magnify) {
                //放大，但不能大于container大小
                if (lp.width + 10/*todo*/ > parent.getLayoutParams().width) {
                    lp.width = parent.getLayoutParams().width;
                    lp.height = parent.getLayoutParams().height;
                } else {
                    lp.width += 10;
                    lp.height += 10;
                }
            } else {
                //还原大小
                lp.width = sItemSize;
                lp.height = sItemSize;
            }
            tv.setLayoutParams(lp);
        }
    }

    @OnClick(R.id.button_finish)
    public void onFinishClick() {

    }

    @OnClick(R.id.img_back)
    public void onCloseClick() {
        fill();
    }
}
