package com.example.jetpackdemo;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpackdemo.databinding.ItemRvBinding;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MHolder> {

    private List<String> mData = new ArrayList<>();

    public void setData(List<String> data) {
        mData.addAll(data);
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRvBinding itemRvBinding = ItemRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MHolder(itemRvBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        String data = mData.get(position);
        holder.mTextView.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MHolder(@NonNull ItemRvBinding binding) {
            super(binding.getRoot());
            mTextView = binding.tvItem;
        }
    }
}
