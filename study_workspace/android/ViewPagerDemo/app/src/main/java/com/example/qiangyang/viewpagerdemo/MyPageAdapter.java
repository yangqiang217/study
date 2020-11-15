package com.example.qiangyang.viewpagerdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.qiangyang.viewpagerdemo.verticalviewpager.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by qiangyang on 2017/9/14.
 */

public class MyPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;

    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @Override
    public int getItemPosition(Object object) {
        System.out.println("getItemPosition");
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("getItem, position: " + position);
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        System.out.println("getCount, count: " + mFragmentList.size());
        return mFragmentList.size();
    }
}
