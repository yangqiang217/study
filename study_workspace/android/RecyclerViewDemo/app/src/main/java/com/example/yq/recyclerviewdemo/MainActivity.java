package com.example.yq.recyclerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yq.recyclerviewdemo.layoutmanager.MLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int HALF_SCREEN = 1920 * 2 / 3;

    private Button btn;
    private RecyclerView recyclerView;
    private List<Bean> mDatas;
    private HomeAdapter mAdapter;

    private float lastY;
    private int moved = 0;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        EventBus.getDefault().register(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new MLayoutManager());
        recyclerView.addItemDecoration(new MyDecoration());
        recyclerView.setAdapter(mAdapter = new HomeAdapter());

        btn = (Button) findViewById(R.id.btnRefresh);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAdapter.notifyDataSetChanged();
                startActivityForResult(new Intent(MainActivity.this, SecondActivity.class), 123);
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
    static int count;
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            System.out.println("onCreateViewHolder called: " + (++count));
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            System.out.println("onBindViewHolder, pos: " + position);
            holder.tv.setText(mDatas.get(position).s1);

            MyOnclickListener listener = new MyOnclickListener();
            holder.btn1.setOnClickListener(listener);
            holder.btn2.setOnClickListener(listener);
            holder.btn3.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            Button btn1;
            Button btn2;
            Button btn3;
            TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.id_num);
                btn1 = (Button) itemView.findViewById(R.id.btn1);
                btn2 = (Button) itemView.findViewById(R.id.btn2);
                btn3= (Button) itemView.findViewById(R.id.btn3);
            }
        }
    }

    private class Bean {
        public Bean(String s1, String s2, String s3) {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
        }

        String s1;
        String s2;
        String s3;
    }

    private class MyOnclickListener implements View.OnClickListener {

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
    }
}
