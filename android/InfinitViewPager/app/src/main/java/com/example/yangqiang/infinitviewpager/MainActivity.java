package com.example.yangqiang.infinitviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private int mCurrentIndex, mImageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.vp);

        //外部只传入一个image list
        List<Integer> resourceList = new ArrayList<>();
        resourceList.add(R.mipmap.a);
        resourceList.add(R.mipmap.b);
        resourceList.add(R.mipmap.c);
        resourceList.add(R.mipmap.d);
        resourceList.add(R.mipmap.e);
        resourceList.add(R.mipmap.f);
        resourceList.add(R.mipmap.g);
        mImageCount = resourceList.size();

        MyAdapter myAdapter = new MyAdapter(fillInnerList(resourceList));
        mViewPager.setAdapter(myAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);//--------关键点-----------
    }

    /**
     * 用外部给的资源的list产生内部实际的list
     * 前后各加1，一进来currentIndex是1，第0个是外部看到的最后一个，第count个是外部看到的首个
     * //--------关键点------------
     */
    private List<View> fillInnerList(List<Integer> resourceList) {
        List<View> innerList = new ArrayList<>();

        int imageCount = resourceList.size();
        int innerCount = imageCount + 2;
        for (int i = 0; i < innerCount; i++) {
            ImageView view = new ImageView(this);
            if (i == 0) {
                view.setImageResource(resourceList.get(imageCount - 1));
            } else if (i == innerCount - 1) {
                view.setImageResource(resourceList.get(0));
            } else {
                view.setImageResource(resourceList.get(i - 1));
            }
            innerList.add(view);
        }

        return innerList;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected, new: " + position);
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //--------关键点-----------
        if (state != ViewPager.SCROLL_STATE_SETTLING) {
            if (mCurrentIndex == 0) {
                mViewPager.setCurrentItem(mImageCount, false);
            } else if (mCurrentIndex == mImageCount + 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }

    private static class MyAdapter extends PagerAdapter {

        private List<View> innerList;

        public MyAdapter(List<View> innerList) {
            this.innerList = innerList;
        }

        @Override
        public int getCount() {
            return innerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(innerList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(innerList.get(position));
            return innerList.get(position);
        }
    }
}
