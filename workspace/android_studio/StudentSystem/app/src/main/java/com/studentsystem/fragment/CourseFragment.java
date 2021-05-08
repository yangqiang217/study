package com.studentsystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.activity.CourseAddActivity;
import com.studentsystem.activity.CourseChooseActivity;
import com.studentsystem.activity.CourseDetailActivity;
import com.studentsystem.activity.CourseEditActivity;
import com.studentsystem.adapter.CourseListAdapter;
import com.studentsystem.bean.Course;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.CourseChangedEvent;
import com.studentsystem.listener.OnCourseItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 课程列表页面
 * 学生登录右下角是选课，可以点击选课
 * 管理员登录右下角是添加课程
 */
public class CourseFragment extends Fragment implements View.OnClickListener, OnCourseItemClickListener {

    private EditText mEtSearch;
    private TextView mTvChooseOrAndCourse;
    private RecyclerView mRvCourseList;

    private CourseListAdapter mCourseListAdapter;

    private boolean mIsStu;

    //跳转过去的位置，可能还会继续进编辑，记着位置编辑了好改变列表数据
    private int mJumpedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.fragment_course, container, false);

        mIsStu = MyApplication.getInstance().isLoginedStu();

        mEtSearch = view.findViewById(R.id.et_search_input);
        mRvCourseList = view.findViewById(R.id.rv_course_list);
        mTvChooseOrAndCourse = view.findViewById(R.id.tv_course_choose_or_add);

        mTvChooseOrAndCourse.setText(mIsStu ? "选课" : "添加课程");
        mTvChooseOrAndCourse.setOnClickListener(this);
        mEtSearch.setHint("请输入课程名关键字");

        view.findViewById(R.id.tv_search_do).setOnClickListener(this);

        initRecyclerView();
        initData();

        return view;
    }

    //初始化列表ui
    private void initRecyclerView() {
        mCourseListAdapter = new CourseListAdapter(getContext(), false);
        mCourseListAdapter.setOnItemClickListener(this);
        mRvCourseList.setAdapter(mCourseListAdapter);
        mRvCourseList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        if (Consts.IS_LOCAL) {
            //去本地数据库查
            List<Course> courseList = DBManager.getInstance().getCourseDao().queryDataList();
            mCourseListAdapter.setDataList(courseList);
        } else {
            // TODO: 2020/6/28
        }
    }

    private void doSearch() {
        String keyWord = mEtSearch.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            initData();
            return;
        }
        if (Consts.IS_LOCAL) {
            List<Course> queryRes = DBManager.getInstance().getCourseDao().queryDataListByName(keyWord);
            mCourseListAdapter.setDataList(queryRes);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_do:
                doSearch();
                break;
            case R.id.tv_course_choose_or_add:
                if (mIsStu) {
                    //学生进入选课页面
                    CourseChooseActivity.start(getActivity(), MyApplication.getInstance().getLoginedStudent());
                } else {
                    //管理员进入课程添加页面
                    Intent intent = new Intent(getActivity(), CourseAddActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onCourseItemClick(int position, Course course) {
        mJumpedPosition = position;
        CourseDetailActivity.start(getContext(), course);
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
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
