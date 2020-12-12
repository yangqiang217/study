package com.example.yq.recyclerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yq.recyclerviewdemo.layoutmanager.MLayoutManager;
import com.example.yq.recyclerviewdemo.utils.CacheInfoPrintPrintUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Bean> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        EventBus.getDefault().register(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new MyDecoration());

        mAdapter = new HomeAdapter();
        mAdapter.setOnClickListener(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                CacheInfoPrintPrintUtil.showCacheInfo(recyclerView);
            }
        });

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAdapter.notifyDataSetChanged();
//                startActivityForResult(new Intent(MainActivity.this, SecondActivity.class), 123);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            CacheInfoPrintPrintUtil.showCacheInfo(recyclerView);
//                            try {
//                                TimeUnit.MILLISECONDS.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveDeleteFeedStreamItem(SecondActivity.InsertEvent event) {
        for (int i = 20; i < 100; i++) {
            mDatas.add(new Bean(i + "", i + "", i + ""));
        }
        mAdapter.notifyItemRangeInserted(20, 80);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveDeleteFeedStreamItem(SecondActivity.ScrollEvent event) {
        recyclerView.scrollToPosition(80);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 321) {
            recyclerView.scrollToPosition(150);
        }
    }

    private void initData() {
        mDatas = new ArrayList<Bean>();
        for (int i = 0; i < 200; i++) {
            mDatas.add(new Bean(i + "", i + "", i + ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                System.out.println("btn1");
                mDatas.set(0, new Bean("aa", "bb", "cc"));
                mAdapter.notifyItemChanged(0);
                break;
            case R.id.btn2:
                System.out.println("btn2");
                break;
            case R.id.btn3:
                System.out.println("btn3");
                break;
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private View.OnClickListener mOnClickListener;
        private int count;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            System.out.println("onCreateViewHolder called: " + (++count));
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            System.out.println("onBindViewHolder, pos: " + position);
            holder.tv.setText(mDatas.get(position).s1);

            holder.btn1.setOnClickListener(mOnClickListener);
            holder.btn2.setOnClickListener(mOnClickListener);
            holder.btn3.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        Button btn1;
        Button btn2;
        Button btn3;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.id_num);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            btn3= itemView.findViewById(R.id.btn3);
        }
    }

    private static class Bean {
        public Bean(String s1, String s2, String s3) {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
        }

        String s1;
        String s2;
        String s3;
    }
}
