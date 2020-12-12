package com.example.yq.mvvmdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yq.mvvmdemo.databinding.ActivitySecondBinding;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SecondActivity extends AppCompatActivity {

    ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_second);
        binding.setImageUrl("http://115.159.198.162:3000/posts/57355a92d9ca741017a28375/1467250338739.jpg");

    }
}
