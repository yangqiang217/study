package com.example.qiangyang.viewpagerdemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qiangyang.viewpagerdemo.fragment.AFragment;
import com.example.qiangyang.viewpagerdemo.fragment.BFragment;
import com.example.qiangyang.viewpagerdemo.fragment.CFragment;
import com.example.qiangyang.viewpagerdemo.verticalviewpager.VerticalViewPager;
import com.example.qiangyang.viewpagerdemo.verticalviewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VerticalViewPager vp;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (VerticalViewPager) findViewById(R.id.vp);
        btn = (Button) findViewById(R.id.btn);

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());

        final MyPageAdapter myPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(myPageAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragments.clear();
                myPageAdapter.notifyDataSetChanged();
            }
        });
    }
}
