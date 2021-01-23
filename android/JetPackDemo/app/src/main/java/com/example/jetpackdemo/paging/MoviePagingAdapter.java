package com.example.jetpackdemo.paging;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpackdemo.bean.Movie;
import com.example.jetpackdemo.databinding.ItemRvBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 三种通用
 */
public class MoviePagingAdapter extends PagedListAdapter<Movie, MoviePagingAdapter.MHolder> {

    private List<Movie> mData = new ArrayList<>();

    /**
     * 用于计算两个数据列表之间的差异
     */
    private  static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {

        /**
         * 当DiffUtil想要检测两个对象是否代表同一个Item时，调用该方法进行判断。
         */
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            Log.d("yqtest", "areItemsTheSame: oldItem: " + oldItem + ", newItem: " + newItem);
            return oldItem.getName().equals(newItem.getName());
        }

        /**
         * 当DiffUtil想要检测两个Item是否存在不一样的数据时，调用该方法进行判断
         */
        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MoviePagingAdapter() {
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

        //要用getItem() 获取数据
        Movie data = getItem(position);
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
