package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.adapter.CourseListAdapter;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.utils.DialogUtil;
import com.studentsystem.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选课页面
 * 可以打勾或者取消打勾，表示选课或者取消选课
 * 加载所有课程，勾选传入的Student已选
 */
public class CourseChooseActivity extends AppCompatActivity {

    private static final String KEY_PARAM_STU = "stu";

    private RecyclerView mRvList;
    private CourseListAdapter mAdapter;
    //导航title
    private TextView mTvTitle;

    //不论学生自己选课还是管理员选课，只需要传一个学生进来
    private Student mStudent;

    public static void start(Activity activity, Student student) {
        Intent intent = new Intent(activity, CourseChooseActivity.class);
        intent.putExtra(KEY_PARAM_STU, student);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);

        mTvTitle = findViewById(R.id.tv_nav_title);
        mRvList = findViewById(R.id.rv_choose_course_list);

        mTvTitle.setText("选课");

        findViewById(R.id.iv_nav_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_choose_course_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Course> chosenList = mAdapter.getChosenList();
                List<Course> unChosenList = mAdapter.getUnChosenList();
                if (chosenList.size() == 0 && unChosenList.size() == 0) {
                    ToastUtil.showToast(CourseChooseActivity.this, "没有选择或取消任何课程");
                    return;
                }
                //确认选课
                DialogUtil.showDialog(CourseChooseActivity.this, "确认选择或取消课程？",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirm();
                        }
                    }
                );
            }
        });

        mStudent = (Student) getIntent().getSerializableExtra(KEY_PARAM_STU);

        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mAdapter = new CourseListAdapter(this, true);
        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void initData() {
        if (Consts.IS_LOCAL) {
            List<Course> allCourseList = DBManager.getInstance().getCourseDao().queryDataList();
            //需要加载所有课程
            //还需要查询已选课程表，把已经选择的课程打勾
            List<StuCourse> stuCourseList = DBManager.getInstance().getStuCourseDao()
                .queryDataListByStuId(mStudent.getStuId());
            mAdapter.setDataList(allCourseList, stuCourseList);
        } else {
            // TODO: 2020/6/27
        }
    }

    private void confirm() {
        if (Consts.IS_LOCAL) {
            List<Course> chosenList = mAdapter.getChosenList();
            List<Course> unChosenList = mAdapter.getUnChosenList();
            //构造可以插入StuCourse表中的数据，mStudent不是空就用mStudent，是空就用当前登录用户id
            //待插入的列表
            List<StuCourse> insertList = new ArrayList<>();
            for (Course course : chosenList) {
                insertList.add(
                    new StuCourse()
                        .setStuId(mStudent.getStuId())
                        .setCourseId(course.getCourseId())
                );
            }

            //待删除的列表
            List<StuCourse> deleteList = new ArrayList<>();
            for (Course course : unChosenList) {
                deleteList.add(
                    new StuCourse()
                        .setStuId(mStudent.getStuId())
                        .setCourseId(course.getCourseId())
                );
            }

            boolean resAdd = DBManager.getInstance().getStuCourseDao().insertData(insertList);
            boolean resDel = DBManager.getInstance().getStuCourseDao().deleteData(deleteList);
            if (resAdd && resDel) {
                ToastUtil.showToast(this, "操作成功");
                mAdapter.clearChosenList();
                mAdapter.clearUnChosenList();
            } else {
                ToastUtil.showToast(this, "数据库插入失败");
            }
        } else {
            // TODO: 2020/6/27  
        }
    }
}