package com.example.jetpackdemo.list;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpackdemo.databinding.ItemRvBinding;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends PagedListAdapter<Movie, MainAdapter.MHolder> {

    private List<Movie> mData = new ArrayList<>();

    private  static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            Log.d("yqtest", "areItemsTheSame: oldItem: " + oldItem + ", newItem: " + newItem);
            return oldItem.getName().equals(newItem.getName());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MainAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setData(List<Movie> data) {
        mData.addAll(data);
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRvBinding itemRvBinding = ItemRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        Log.d("yqtest", "onCreateViewHolder");
        return new MHolder(itemRvBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
//        Log.d("yqtest", "onBindViewHolder: pos: "+ position);

        Movie data = getItem(position);//getitem 获取数据
        if (data != null) {
            holder.mTvName.setText(data.getName());
            holder.mTvPrice.setText(String.valueOf(data.getPrice()));
        }
    }

    static class MHolder extends RecyclerView.ViewHolder {

        private TextView mTvName, mTvPrice;

        public MHolder(@NonNull ItemRvBinding binding) {
            super(binding.getRoot());
            mTvName = binding.tvName;
            mTvPrice = binding.tvPrice;
        }
    }
}
