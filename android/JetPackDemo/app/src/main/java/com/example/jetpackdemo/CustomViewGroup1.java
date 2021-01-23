package com.example.jetpackdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jetpackdemo.databinding.LayoutCustom1Binding;

public class CustomViewGroup1 extends RelativeLayout {

    private LayoutCustom1Binding mBinding;

    public CustomViewGroup1(Context context) {
        super(context);
        init(context);
    }

    public CustomViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mBinding = LayoutCustom1Binding.inflate(LayoutInflater.from(context), this, true);
        mBinding.btnCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "custom show", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }
}
