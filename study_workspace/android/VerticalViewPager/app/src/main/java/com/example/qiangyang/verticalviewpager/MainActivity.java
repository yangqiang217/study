package com.example.qiangyang.verticalviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.qiangyang.verticalviewpager.ItemFragment.Bean;
import com.example.qiangyang.verticalviewpager.views.BaseFragmentStatePagerAdapter;
import com.example.qiangyang.verticalviewpager.views.VerticalViewPager;
import com.example.qiangyang.verticalviewpager.views.ViewPager;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int COUNT = 20;

    private VerticalViewPager verticalViewPager;
    private List<ItemFragment> fragmentList = new ArrayList<>();

    private List<Bean> dataList;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        initData();
        initFragment();

        verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalViewpager);

        verticalViewPager.setAdapter(new RecyclerPagerAdapter(getSupportFragmentManager()));
//        verticalViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                System.out.println("onPageScrolled, position: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//                fragmentList.get(position).setData(dataList.get(position));
//                System.out.println("onPageSelected, position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                System.out.println("onPageScrollStateChanged, state: " + state);
            }
        });
    }

    private void initFragment() {
        for (int i = 0; i < COUNT; i++) {
            ItemFragment itemFragment = ItemFragment.getInstance(dataList.get(i));
            itemFragment.setBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(((Button)v).getText().toString());
                }
            });
            fragmentList.add(itemFragment);
        }
    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            Bean bean = new Bean("btn" + i, "tv" + i, ImageUriUtils.getImageUrl());
            dataList.add(bean);
        }
    }

    private class RecyclerPagerAdapter extends BaseFragmentStatePagerAdapter {

        public RecyclerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
