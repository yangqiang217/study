package com.example.qiangyang.loopviewpager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoopViewPager viewpager = (LoopViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new SamplePagerAdapter());
    }

    public class SamplePagerAdapter extends PagerAdapter {
        private final Random random = new Random();
        private int mSize;

        public SamplePagerAdapter() {
            mSize = 3;
        }

        public SamplePagerAdapter(int count) {
            mSize = count;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            Log("destroyItem", "position: " + position);
            view.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            Log("instantiateItem", "position: " + position);

            TextView textView = new TextView(view.getContext());

            textView.setText(position + 1 + "");
            textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(50);
            view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return textView;
        }

        // 增加item
        public void addItem() {
            mSize++;
            notifyDataSetChanged();
        }

        // 删除item
        public void removeItem() {
            mSize--;
            mSize = mSize < 0 ? 0 : mSize;

            notifyDataSetChanged();
        }
    }

    public static void Log(String loc, String msg) {
        Log.i(TAG, loc + ", " + msg);
    }
}
