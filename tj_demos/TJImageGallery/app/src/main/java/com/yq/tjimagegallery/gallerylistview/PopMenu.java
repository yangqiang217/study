
package com.yq.tjimagegallery.gallerylistview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yq.tjimagegallery.R;

import java.util.ArrayList;
import java.util.Collections;

public class PopMenu {
    private ArrayList<String> mArrayList;
    private Context mContext;
    private final PopupWindow mPopupWindow;
    private ViewGroup mNearPopuView;
    private ListView mListView;
    private View mPopmenuLayout;

    private final int POSITION = 1000;
    private int mnCurrentPos = POSITION;
    private boolean mShowSelectBtn;
    private ImageView mRightArrow;

    private float density;

    public PopMenu (Context context) {
        this.mContext = context;

        density = mContext.getResources().getDisplayMetrics().density;

        mArrayList = new ArrayList<String>();
        View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);
        mListView = (ListView) view.findViewById(R.id.listView);
        mPopmenuLayout = view.findViewById(R.id.popup_view_cont);
        mListView.setAdapter(new PopAdapter());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 272, mContext.getResources()
                .getDisplayMetrics());
        mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));

        mPopupWindow.setTouchable(true); // 设置可点击
        mPopupWindow.setOutsideTouchable(true); // 设置外层可点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(0);
        mPopupWindow.update();
        mShowSelectBtn = false;

        mPopupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Rect r = new Rect();
                        mListView.getFocusedRect(r);
                        if (!r.contains((int) event.getX(), (int) event.getY())) {
                            dismiss();
                            return true;
                        } else {
                        }

                        break;
                }
                return false;
            }
        });

    }

    public void setOutsideTouch(ImageView ImgView, int selectPon) {
        final ImageView v = ImgView;
        final int position = selectPon;

        mPopupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if (mnCurrentPos == position) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.tab_arrow_down_2x);
                        mnCurrentPos = POSITION;
                        dismiss();
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public void setCurrentPosition(int position) {
        mnCurrentPos = position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(listener);
    }

    public void addItems(String[] items) {
        Collections.addAll(mArrayList, items);
    }

    public void addItem(String item) {
        mArrayList.add(item);
    }
    
    public void setItems(String[] items) {
        mArrayList.clear();
        Collections.addAll(mArrayList, items);
    }

    public void showAsDropDown(View parent) {
        Animation mTipViewAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dlg_top_down);// 使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件
        mPopmenuLayout.startAnimation(mTipViewAnimation);// 开始动画播出

        int xPos = -mPopupWindow.getWidth() / 2;
        mPopupWindow.showAsDropDown(parent, 104, 0);
    }

    public void showAsLeft(View parent) {

        int xPos = mPopupWindow.getWidth() / 2;
        mPopupWindow.showAsDropDown(parent, xPos / 4, 5);
    }

    public void showAsRight(View parent) {

        int xPos = -mPopupWindow.getWidth() / 2;
        mPopupWindow.showAsDropDown(parent, xPos, 5);
    }

    public void dismiss() {
        Animation mTipViewAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dlg_top_up);// 使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件
        mTipViewAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // The animation has ended
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mPopupWindow.dismiss();
                    }
                });

            }
        });
        mPopmenuLayout.startAnimation(mTipViewAnimation);// 开始动画播出

    }

    public void showSelectBtn(boolean show) {
        mShowSelectBtn = show;
    }

    private final class PopAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.pomenu_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.layout = (RelativeLayout) convertView.findViewById(R.id.pop_layout);
                holder.groupItem = (TextView) convertView.findViewById(R.id.textView);
                holder.select_iImageView = (ImageView) convertView.findViewById(R.id.select_one_item);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.groupItem.setText(mArrayList.get(position));
            if (mShowSelectBtn) {
                if (mnCurrentPos == position) {
                    holder.select_iImageView.setImageResource(R.drawable.blue_checkbox_selected);
                } else {
                    holder.select_iImageView.setImageResource(R.drawable.blue_checkbox_default);
                }
                holder.select_iImageView.setVisibility(View.VISIBLE);
            } else {
                holder.select_iImageView.setVisibility(View.GONE);
                if (mnCurrentPos == position) {
                    holder.layout.setBackgroundResource(R.drawable.popmenu_select);
                    holder.layout.setPadding((int) (density * 12), 0, 0, 0);
                } else {
                    holder.layout.setBackgroundColor(mContext.getResources().getColor(
                            R.color.white));
                    holder.layout.setPadding((int) (density * 12), 0, 0, 0);
                }
            }

            return convertView;
        }

        private final class ViewHolder {
            RelativeLayout layout;
            TextView groupItem;
            ImageView select_iImageView;
        }
    }
}
