package com.yq.listviewdiffitem;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }

        MyAdapter myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private final int TYPE1 = 1;
        private final int TYPE2 = 2;
        private final int TYPE3 = 3;

        MyAdapter() {
            inflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            switch (position % 3) {
                case 0:
                    return TYPE1;
                case 1:
                    return TYPE2;
                case 2:
                    return TYPE3;
                default:
                    return TYPE1;
            }
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
            ViewHolder1 viewHolder1 = null;
            ViewHolder2 viewHolder2 = null;
            ViewHolder3 viewHolder3 = null;

            final int type = getItemViewType(position);

            if (convertView == null) {
                switch (type) {
                    case TYPE1:
                        convertView = inflater.inflate(R.layout.item_type1, null);
                        viewHolder1 = buildHolder1(convertView);
                        break;
                    case TYPE2:
                        convertView = inflater.inflate(R.layout.item_type2, null);
                        viewHolder2 = buildHolder2(convertView);
                        break;
                    case TYPE3:
                        convertView = inflater.inflate(R.layout.item_type3, null);
                        viewHolder3 = buildHolder3(convertView);
                        break;
                }
            } else {
                switch (type) {
                    case TYPE1:
                        viewHolder1 = (ViewHolder1) convertView.getTag(R.id.cbType1);
                        if (viewHolder1 == null) {
                            viewHolder1 = buildHolder1(convertView);
                        }
                        break;
                    case TYPE2:
                        viewHolder2 = (ViewHolder2) convertView.getTag(R.id.btnType2);
                        if (viewHolder2 == null) {
                            viewHolder2 = buildHolder2(convertView);
                        }
                        break;
                    case TYPE3:
                        viewHolder3 = (ViewHolder3) convertView.getTag(R.id.btnType3);
                        if (viewHolder3 == null) {
                            viewHolder3 = buildHolder3(convertView);
                        }
                        break;
                }
            }

            switch (type) {
                case TYPE1:
                    viewHolder1.checkBox.setText(position + "");
                    viewHolder1.tv.setText(position + "");
                    break;
                case TYPE2:
                    viewHolder2.btn.setText("Btn: " + position);
                    break;
                case TYPE3:
                    viewHolder3.btn.setText("type3 btn: " + position);
                    viewHolder3.tv.setText("type3 tv: " + position);
                    break;
            }
            return convertView;
        }
    }

    private ViewHolder1 buildHolder1(View convertView) {
        ViewHolder1 viewHolder1 = new ViewHolder1();
        viewHolder1.checkBox = (CheckBox) convertView.findViewById(R.id.cbType1);
        viewHolder1.tv = (TextView) convertView.findViewById(R.id.tvType1);
        convertView.setTag(R.id.cbType1, viewHolder1);
        return viewHolder1;
    }

    private ViewHolder2 buildHolder2(View convertView) {
        ViewHolder2 viewHolder2 = new ViewHolder2();
        viewHolder2.btn = (Button) convertView.findViewById(R.id.btnType2);
        convertView.setTag(R.id.btnType2, viewHolder2);
        return viewHolder2;
    }

    private ViewHolder3 buildHolder3(View convertView) {
        ViewHolder3 viewHolder3 = new ViewHolder3();
        viewHolder3.btn = (Button) convertView.findViewById(R.id.btnType3);
        viewHolder3.tv = (TextView) convertView.findViewById(R.id.tvType3);
        convertView.setTag(R.id.btnType3, viewHolder3);
        return viewHolder3;
    }

    private static class ViewHolder1 {
        CheckBox checkBox;
        TextView tv;
    }
    private static class ViewHolder2 {
        Button btn;
    }
    private static class ViewHolder3 {
        Button btn;
        TextView tv;
    }
}

