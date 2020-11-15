package com.yq.eventdispatch.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yq.eventdispatch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListViewActivity extends Activity {

    private ListView lv;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MyAdapter() {
            inflater = LayoutInflater.from(ListViewActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item, null);
                holder = new Holder();
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tv.setText(list.get(position) + "");
            return convertView;
        }
    }

    static class Holder {
        TextView tv;
    }
}
