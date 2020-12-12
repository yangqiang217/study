package com.example.qiangyang.statusbardemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private int mAlpha;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        StatusBarUtil.setTranslucentForImageView(MainActivity.this, 0, btn);
    }
}
