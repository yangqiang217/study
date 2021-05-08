package com.example.yangqiang.test.activity.list;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangqiang.test.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<String> data = new ArrayList<>();

        public void setData(List<String> d) {
            data.addAll(d);
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int i) {
            final String s = data.get(i);

            holder.mTv.setText(s);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("listcompare", "recycler onclick");
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView mTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.name);
        }
    }
}