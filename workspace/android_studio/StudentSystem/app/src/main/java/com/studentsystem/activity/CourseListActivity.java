package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.studentsystem.R;
import com.studentsystem.adapter.CourseListAdapter;
import com.studentsystem.bean.Course;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.CourseChangedEvent;
import com.studentsystem.listener.OnCourseItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

/**
 * 课程列表页，比如我的选课、管理员的授课，点击列表能看详情，todo 详情能编辑吗
 */
public class CourseListActivity extends AppCompatActivity implements View.OnClickListener, OnCourseItemClickListener {

    private static final String KEY_PARAM = "course_list";

    private TextView mTvTitle;
    private RecyclerView mRvCourseList;

    private CourseListAdapter mCourseListAdapter;

    //跳转过去的位置，可能还会继续进编辑，记着位置编辑了好改变列表数据
    private int mJumpedPosition = -1;

    private List<Course> mCourseList;

    /**
     * @param courseList 数据量应该不会很大，所以用Intent传
     */
    public static void start(Context context, List<Course> courseList) {
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.putExtra(KEY_PARAM, (Serializable) courseList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        EventBus.getDefault().register(this);

        mCourseList = (List<Course>) getIntent().getSerializableExtra(KEY_PARAM);

        mTvTitle = findViewById(R.id.tv_nav_title);
        mRvCourseList = findViewById(R.id.rv_course_list);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);

        mTvTitle.setText("课程列表");

        initRecyclerView();
    }

    //初始化列表ui
    private void initRecyclerView() {
        mCourseListAdapter = new CourseListAdapter(this, false);
        mCourseListAdapter.setOnItemClickListener(this);
        mRvCourseList.setAdapter(mCourseListAdapter);
        mRvCourseList.setLayoutManager(new LinearLayoutManager(this));
        mCourseListAdapter.setDataList(mCourseList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
        }
    }

    @Override
    public void onCourseItemClick(int position, Course course) {
        mJumpedPosition = position;
        CourseDetailActivity.start(this, course);
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveCourseChangeEvent(CourseChangedEvent event) {
        switch (event.getAction()) {
            case CourseChangedEvent.ACTION_ADD:
                mCourseListAdapter.insertItem(0, event.getCourse());
                mRvCourseList.scrollToPosition(0);
                break;
            case CourseChangedEvent.ACTION_EDIT:
                mCourseListAdapter.changeItem(mJumpedPosition, event.getCourse());
                break;
            case CourseChangedEvent.ACTION_DELETE:
                mCourseListAdapter.deleteItem(mJumpedPosition);
                break;
        }
        mJumpedPosition = -1;//修改了后还原
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}