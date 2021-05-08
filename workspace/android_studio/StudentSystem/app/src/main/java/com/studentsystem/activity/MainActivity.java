package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.fragment.CourseFragment;
import com.studentsystem.fragment.MyFragment;
import com.studentsystem.fragment.StudentFragment;

/**
 * 首页，包括两个(学生)或三个(管理员)tab页面的切换逻辑，本身ui只包括底部导航
 * 学生不显示"学生"tab
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //每个tab页面的标记
    private static final String TAG_COURSE = "course", TAG_STUDENT = "student", TAG_MY = "my";

    private TextView mTvCourse, mTvStudent, mTvMy;
    private ImageView mIvCourse, mIvStudent, mIvMy;
    private ViewGroup mLlCourse, mLlStudent, mLlMy;
    private int mCurrentTab;//标记当前是那个标签

    private Fragment[] mMainFragments = new Fragment[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLlCourse = findViewById(R.id.ll_main_cource);
        mLlStudent = findViewById(R.id.ll_main_student);
        mLlMy = findViewById(R.id.ll_main_my);

        mTvCourse = findViewById(R.id.tv_main_cource);
        mTvStudent = findViewById(R.id.tv_main_student);
        mTvMy = findViewById(R.id.tv_main_my);

        mIvCourse = findViewById(R.id.iv_main_cource);
        mIvStudent = findViewById(R.id.iv_main_student);
        mIvMy = findViewById(R.id.iv_main_my);

        //给底部三个按钮设置点击事件
        mLlCourse.setOnClickListener(this);
        mLlStudent.setOnClickListener(this);
        mLlMy.setOnClickListener(this);

        if (MyApplication.getInstance().isLoginedStu()) {
            mLlStudent.setVisibility(View.GONE);
        }

        //第一次进去要显示推荐页面
        showFragment(TAG_COURSE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_main_cource:
                mCurrentTab = 0;
                changeColor();
                showFragment(TAG_COURSE);
                break;
            case R.id.ll_main_student:
                mCurrentTab = 1;
                changeColor();
                showFragment(TAG_STUDENT);
                break;
            case R.id.ll_main_my:
                mCurrentTab = 2;
                changeColor();
                showFragment(TAG_MY);
                break;
        }
    }


    /**
     * 当前选中的标签颜色变红，其它两个颜色变灰
     */
    private void changeColor() {
        switch (mCurrentTab) {
            case 0:
                mTvCourse.setTextColor(getResources().getColor(R.color.main_color));
                mTvStudent.setTextColor(getResources().getColor(R.color.main_tab_unselect));
                mTvMy.setTextColor(getResources().getColor(R.color.main_tab_unselect));

                mIvCourse.setImageResource(R.mipmap.tabbar_icon_course_hl);
                mIvStudent.setImageResource(R.mipmap.tabbar_icon_stu);
                mIvMy.setImageResource(R.mipmap.tabbar_icon_mine);
                break;
            case 1:
                mTvCourse.setTextColor(getResources().getColor(R.color.main_tab_unselect));
                mTvStudent.setTextColor(getResources().getColor(R.color.main_color));
                mTvMy.setTextColor(getResources().getColor(R.color.main_tab_unselect));

                mIvCourse.setImageResource(R.mipmap.tabbar_icon_course);
                mIvStudent.setImageResource(R.mipmap.tabbar_icon_stu_hl);
                mIvMy.setImageResource(R.mipmap.tabbar_icon_mine);
                break;
            case 2:
                mTvCourse.setTextColor(getResources().getColor(R.color.main_tab_unselect));
                mTvStudent.setTextColor(getResources().getColor(R.color.main_tab_unselect));
                mTvMy.setTextColor(getResources().getColor(R.color.main_color));

                mIvCourse.setImageResource(R.mipmap.tabbar_icon_course);
                mIvStudent.setImageResource(R.mipmap.tabbar_icon_stu);
                mIvMy.setImageResource(R.mipmap.tabbar_icon_mine_hl);
                break;
        }
    }

    private void showFragment(String tabFragTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mMainFragments[mCurrentTab] == null) {
            Fragment fragment;
            switch (mCurrentTab) {
                case 0:
                    fragment = new CourseFragment();
                    break;
                case 1:
                    fragment = new StudentFragment();
                    break;
                case 2:
                    fragment = new MyFragment();
                    break;
                default: return;
            }
            mMainFragments[mCurrentTab] = fragment;
            transaction.add(R.id.fl_main_fragcontainer, fragment, tabFragTag);
        }

        //隐藏之前的fragment
        for (int i = 0; i < mMainFragments.length; i++) {
            if (i != mCurrentTab && mMainFragments[i] != null && !mMainFragments[i].isHidden()) {
                transaction.hide(mMainFragments[i]);
            }
        }
        transaction.show(mMainFragments[mCurrentTab]);
        transaction.commitAllowingStateLoss();
    }
}