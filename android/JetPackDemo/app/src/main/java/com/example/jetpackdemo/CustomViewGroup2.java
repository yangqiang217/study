package com.example.jetpackdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jetpackdemo.databinding.LayoutCustom2Binding;

public class CustomViewGroup2 {

    private LayoutCustom2Binding mBinding;

    public CustomViewGroup2(Context context, ViewGroup container) {
        init(context, container);
    }

    private void init(Context context, ViewGroup container) {
        mBinding = LayoutCustom2Binding.inflate(LayoutInflater.from(context), container, true);
        mBinding.btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "custom 22 show", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }
}
