package com.example.yangqiang.test.activity.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangqiang.test.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("listcompare", "listview onclick");
            }
        });
        MyAdapter adapter = new MyAdapter(this, R.layout.item_listview);
        listView.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    public static class MyAdapter extends BaseAdapter {

        private int resourceId;
        private Context mContext;
        private List<String> data = new ArrayList<>();

        public MyAdapter(Context context, int resourceId) {
            this.resourceId = resourceId;
            mContext = context;
        }

        public void setData(List<String> d){
            data.addAll(d);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            System.out.println("getView, pos: " + position);
            String s = data.get(position);
            View view;
            MyHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
                holder = new MyHolder();
                holder.tv = (TextView) view.findViewById(R.id.tv_item_listview);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (MyHolder) view.getTag();
            }

            holder.tv.setText(s);
            return view;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    static class MyHolder {
        TextView tv;
    }
}