package com.example.yq.recyclerviewdemo.layoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import com.example.yq.recyclerviewdemo.utils.ViewUtils;


public class MLayoutManager extends RecyclerView.LayoutManager {

    private int totalHeight;
    private int verticalScrollOffset;

    private SparseArray<Rect> allItemRects = new SparseArray<>();
    private SparseBooleanArray itemStates = new SparseBooleanArray();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        System.out.println("onLayoutChildren");

        super.onLayoutChildren(recycler, state);
        // 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
        // 实际就是把View放到了Recycler中的一个集合中。
        detachAndScrapAttachedViews(recycler);
        calculateChildrenSite(recycler);
        recycleAndFillView(recycler, state);
    }

    private void calculateChildrenSite(RecyclerView.Recycler recycler) {
        //单列版
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            // 遍历Recycler中保存的View取出来
            View view = recycler.getViewForPosition(i);
            addView(view); // 因为刚刚进行了detach操作，所以现在可以重新添加
            measureChildWithMargins(view, 0, 0); // 通知测量view的margin值
            int width = getDecoratedMeasuredWidth(view); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
            int height = getDecoratedMeasuredHeight(view);

            Rect mTmpRect = allItemRects.get(i);
            if (mTmpRect == null) {
                mTmpRect = new Rect();
            }
            //调用这个方法能够调整ItemView的大小，以除去ItemDecorator。
            calculateItemDecorationsForChild(view, mTmpRect);

            // 调用这句我们指定了该View的显示区域，并将View显示上去，此时所有区域都用于显示View，
            //包括ItemDecorator设置的距离。
            layoutDecorated(view, 0, totalHeight, width, totalHeight + height);
            totalHeight += height;

            System.out.println("mTmpRect: " + mTmpRect);
            allItemRects.put(i, mTmpRect);
            itemStates.put(i, false);
        }

        //双列版
//        totalHeight = 0;
//        for (int i = 0; i < getItemCount(); i++) {
//            View view = recycler.getViewForPosition(i);
//            addView(view);// 因为刚刚进行了detach操作，所以现在可以重新添加
//            measureChildWithMargins(view, ViewUtils.getScreenWidth() / 2, 0);
//            int width = getDecoratedMeasuredWidth(view);// 计算view实际大小，包括了ItemDecorator中设置的偏移量。
//            int height = getDecoratedMeasuredHeight(view);
//
//            Rect tmpRect = new Rect();
//            //调用这个方法能够调整ItemView的大小，以除去ItemDecorator。
//            calculateItemDecorationsForChild(view, tmpRect);
//
//            if (i % 2 == 0) {
//                layoutDecoratedWithMargins(view, 0, totalHeight, ViewUtils.getScreenWidth() / 2, totalHeight + height);
//            } else {
//                layoutDecoratedWithMargins(view, ViewUtils.getScreenWidth() / 2, totalHeight, ViewUtils.getScreenWidth(), totalHeight + height);
//                totalHeight += height;
//            }
//        }
    }

    private void recycleAndFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 当前scroll offset状态下的显示区域
        Rect displayRect = new Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            //这个方法获取的是RecyclerView中的View，注意区别Recycler中的View
            //这获取的是实际的View
            View child = getChildAt(i);
            //下面几个方法能够获取每个View占用的空间的位置信息，包括ItemDecorator
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler);
                itemStates.put(i, false);
            }
        }

        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {
            //重新显示需要出现在屏幕的子View
            if (Rect.intersects(displayRect, allItemRects.get(i))) {
                //重新显示需要出现在屏幕的子View
                View itemView = recycler.getViewForPosition(i);
                measureChildWithMargins(itemView, ViewUtils.getScreenWidth() / 2, 0);
                //添加View到RecyclerView上
                addView(itemView);
                //取出先前存好的ItemView的位置矩形
                Rect rect = allItemRects.get(i);
                //取出先前存好的ItemView的位置矩形
                layoutDecoratedWithMargins(itemView,
                    rect.left,
                    rect.top - verticalScrollOffset,//因为现在是复用View，所以想要显示在
                    rect.right,
                    rect.bottom - verticalScrollOffset);
                itemStates.put(i, true);//更新该View的状态为依附
            }
        }
        System.out.println("itemCount: " + getChildCount());
    }

    //scroll==========
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
        //实际要滑动的距离
        int travel = dy;

        System.out.println("scrollVerticallyBy, dy: " + dy);

        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        verticalScrollOffset += travel;

        offsetChildrenVertical(-travel);

        return travel;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
