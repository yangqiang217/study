package com.yq.testanimation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SecondActivity extends Activity {

    public static final String TAG = SecondActivity.class.getSimpleName();

    private Button btnHeader;
    private ListView lv;

    private ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();
        initData();

        lv.setAdapter(new MyAdapter(this, dataList));
        lv.setOnScrollListener(new MyScrollListener());
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        btnHeader = (Button) findViewById(R.id.btnHeader);
    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add("ITEM  " + i);
        }
    }

    private static class MyAdapter extends BaseAdapter {

        private ArrayList<String> dataList;
        private LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<String> dataList) {
            this.dataList = dataList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder.btn = (Button) convertView.findViewById(R.id.btnListItem);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.btn.setText(dataList.get(position));
            return convertView;
        }
    }

    private static class ViewHolder {
        public Button btn;
    }

    private void changeHeaderButtonSize(int dis) {
        btnHeader.getLayoutParams().width = 1000 - dis;
        btnHeader.getLayoutParams().height = 300 - dis;
//        System.out.println("dis: " + dis + ", btnHeader width: " + btnHeader.getLayoutParams().width);
        btnHeader.requestLayout();
    }

    private class MyScrollListener implements AbsListView.OnScrollListener {

        private SparseArray<ItemRecord> recordSp = new SparseArray<ItemRecord>(0);
        private int currentFirstVisibleItem = 0;


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            System.out.println("firstVisibleItem: " + firstVisibleItem + ", visibleItemCount: " + visibleItemCount + ", totalItemCount: " + totalItemCount);
            currentFirstVisibleItem = firstVisibleItem;
            View firstView = view.getChildAt(0);
            if (firstView != null) {
                ItemRecord itemRecord = recordSp.get(firstVisibleItem);
                if (itemRecord == null) {
                    itemRecord = new ItemRecord();
                }
                itemRecord.height = firstView.getHeight();
                itemRecord.top = firstView.getTop();
                System.out.println("firstView.getTop()" + firstView.getTop());
                recordSp.append(firstVisibleItem, itemRecord);
                int dis = getScrollY();
                changeHeaderButtonSize(dis);
            }
        }

        //获取滚动高度
        private int getScrollY() {
            int height = 0;
            for (int i = 0; i < currentFirstVisibleItem; i++) {
                ItemRecord itemRecord = recordSp.get(i);
                height += itemRecord.height;
            }
            ItemRecord itemRecord = recordSp.get(currentFirstVisibleItem);
            if (itemRecord == null) {
                itemRecord = new ItemRecord();
            }
            return height - itemRecord.top;
        }
    }

    static class ItemRecord {
        int height = 0;
        int top = 0;
    }
}
