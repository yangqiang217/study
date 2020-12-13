package com.my.glidedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.iv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MainActivity.this)
                    .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607841059979&di=99ee3ab17250509bd7516edb35402629&imgtype=0&src=http%3A%2F%2Fku.90sjimg.com%2Felement_origin_min_pic%2F01%2F59%2F30%2F665748568d367c2.jpg")
                    .placeholder(R.mipmap.ic_launcher)
                    .skipMemoryCache(true)//关闭内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭硬盘缓存
                    .transform(new GlideRoundTransform(MainActivity.this))
                    .into(mImageView);
            }
        });
    }
}