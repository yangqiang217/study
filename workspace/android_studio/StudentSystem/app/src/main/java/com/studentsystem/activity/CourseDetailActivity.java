package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.CourseChangedEvent;
import com.studentsystem.utils.DialogUtil;
import com.studentsystem.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程详情页，从我的课程页面进来可以看到成绩
 * 管理员不能到这个页面，管理员直接回到课程编辑页面CourseEditActivity
 */
public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //intent 传参用的key
    private static final String KEY_PARAM_COURSE = "course";
    private static final String KEY_PARAM_SHOW_SCORE = "show_score";
    private static final String KEY_PARAM_SCORE = "score";
    private static final String KEY_PARAM_CREDIT = "credit";

    //导航title
    private TextView mTvTitle;
    private TextView mTvName, mTvId, mTvTotalTime, mTvCredit, mTvTeacher, mTvObligatory, mTvMyScore, mTvMyCredit;
    private ViewGroup mVgMyScore, mVgMyCredit, mVgBtn;

    private boolean mIsLoginedStu;
    private Course mCourse;

    public static void start(Context context, Course course) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(KEY_PARAM_COURSE, course);
        context.startActivity(intent);
    }

    public static void start(Context context, Course course, float score, float credit) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(KEY_PARAM_COURSE, course);
        intent.putExtra(KEY_PARAM_SHOW_SCORE, true);
        intent.putExtra(KEY_PARAM_SCORE, score);
        intent.putExtra(KEY_PARAM_CREDIT, credit);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        EventBus.getDefault().register(this);

        mCourse = (Course) getIntent().getSerializableExtra(KEY_PARAM_COURSE);
        boolean showScore = getIntent().getBooleanExtra(KEY_PARAM_SHOW_SCORE, false);
        float score = getIntent().getFloatExtra(KEY_PARAM_SCORE, 0);
        float credit = getIntent().getFloatExtra(KEY_PARAM_CREDIT, 0);

        mIsLoginedStu = MyApplication.getInstance().isLoginedStu();

        mTvTitle = findViewById(R.id.tv_nav_title);

        mTvName = findViewById(R.id.tv_course_detail_name);
        mTvId = findViewById(R.id.tv_course_detail_id);
        mTvTotalTime = findViewById(R.id.tv_course_detail_time);
        mTvCredit = findViewById(R.id.tv_course_detail_credit);
        mTvTeacher = findViewById(R.id.tv_course_detail_teacher);
        mTvObligatory = findViewById(R.id.tv_course_detail_obligatory);
        mTvMyScore = findViewById(R.id.tv_course_detail_myscore);
        mTvMyCredit = findViewById(R.id.tv_course_detail_mycredit);

        mVgMyScore = findViewById(R.id.rl_course_detail_myscore);
        mVgMyCredit = findViewById(R.id.rl_course_detail_mycredit);
        mVgBtn = findViewById(R.id.ll_course_detail_btn);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);
        findViewById(R.id.tv_course_detail_edit).setOnClickListener(this);
        findViewById(R.id.tv_course_detail_chosen_course).setOnClickListener(this);
        findViewById(R.id.tv_course_detail_delete).setOnClickListener(this);

        mVgBtn.setVisibility(mIsLoginedStu ? View.GONE : View.VISIBLE);

        if (showScore) {
            mVgMyScore.setVisibility(View.VISIBLE);
            mVgMyCredit.setVisibility(View.VISIBLE);
            mTvMyScore.setText(String.valueOf(score));
            mTvMyCredit.setText(String.valueOf(credit));
        } else {
            mVgMyScore.setVisibility(View.GONE);
            mVgMyCredit.setVisibility(View.GONE);
        }

        fillUI();
    }

    private void fillUI() {
        //用Course填充UI
        mTvTitle.setText("课程详情");
        mTvName.setText(mCourse.getName());
        mTvId.setText(mCourse.getCourseId());
        mTvTotalTime.setText(mCourse.getTotalTime() + "学时");
        mTvCredit.setText(mCourse.getCredit() + "学分");
        mTvTeacher.setText(mCourse.getTeacher());
        mTvObligatory.setText(mCourse.getObligatory());
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变这个页面数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveCourseEditedEvent(CourseChangedEvent event) {
        mCourse = event.getCourse();
        fillUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_course_detail_edit:
                CourseEditActivity.start(this, mCourse);
                break;
            case R.id.tv_course_detail_chosen_course:
                gotoStudent();
                break;
            case R.id.tv_course_detail_delete:
                //删除此课程
                DialogUtil.showDialog(this, "确认删除本课程？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });
                break;
        }
    }

    /**
     * 进入选择此课程的学生页面
     */
    private void gotoStudent() {
        if (Consts.IS_LOCAL) {
            //查询已选课程的学生
            List<StuCourse> stuCourseList = DBManager.getInstance().getStuCourseDao()
                .queryDataListByCourseId(mCourse.getCourseId());
            if (stuCourseList == null || stuCourseList.size() == 0) {
                ToastUtil.showToast(this, "暂无学生选此课程");
                return;
            }
            //构造成dao可以用的形式，并计算总成绩
            List<String> stuIds = new ArrayList<>();
            for (StuCourse stuCourse : stuCourseList) {
                //给查数据库的list添加
                stuIds.add(stuCourse.getStuId());
            }
            List<Student> students = DBManager.getInstance().getStudentDao().queryDataListByIds(stuIds);
            StudentListActivity.start(this, mCourse, students);
        } else {
            // TODO: 2020/6/27
        }
    }

    private void delete() {
        if (Consts.IS_LOCAL) {
            //在本地数据库删除
            DBManager.getInstance().getCourseDao().deleteData(mCourse.getCourseId());
            EventBus.getDefault().post(new CourseChangedEvent(CourseChangedEvent.ACTION_DELETE, mCourse));
            ToastUtil.showToast(MyApplication.getInstance(), "删除成功");
            finish();
        } else {
            // TODO: 2020/6/27
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}