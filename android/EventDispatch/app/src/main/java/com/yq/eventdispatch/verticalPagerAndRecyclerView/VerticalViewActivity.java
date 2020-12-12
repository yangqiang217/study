package com.yq.eventdispatch.verticalPagerAndRecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.yq.eventdispatch.R;

import java.util.ArrayList;
import java.util.List;

import static com.yq.eventdispatch.R.id.act_videoplay_verticalviewpager;

public class VerticalViewActivity extends AppCompatActivity {

    public static VerticalViewPager verticalViewPager;
    private List<Fragment> list = new ArrayList<>();
    private ViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_view);

        verticalViewPager = (VerticalViewPager) findViewById(act_videoplay_verticalviewpager);
        adapter = new ViewpagerAdapter(getSupportFragmentManager());
        verticalViewPager.setAdapter(adapter);

        list.add(new MyFragment());
        list.add(new MyFragment());
        list.add(new MyFragment());
        adapter.notifyDataSetChanged();
    }


    private class ViewpagerAdapter extends FragmentStatePagerAdapter {

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
