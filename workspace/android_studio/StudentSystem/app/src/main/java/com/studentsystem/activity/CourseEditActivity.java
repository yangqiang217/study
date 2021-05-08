package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.CourseChangedEvent;
import com.studentsystem.utils.DialogUtil;
import com.studentsystem.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 课程编辑页面，管理员点击课程进入的不是课程详情而是这个页面，直接可编辑
 * 只有管理员才能进来
 */
public class CourseEditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_PARAM = "course";

    //导航title
    private TextView mTvTitle;
    private EditText mEtName, mEtId, mEtTotalTime, mEtCredit, mEtTeacher;
    private RadioButton mRbObligatory, mRbElective;

    private Course mCourse;

    public static void start(Context context, Course course) {
        Intent intent = new Intent(context, CourseEditActivity.class);
        intent.putExtra(KEY_PARAM, course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        mCourse = (Course) getIntent().getSerializableExtra(KEY_PARAM);

        mTvTitle = findViewById(R.id.tv_nav_title);

        mEtName = findViewById(R.id.et_course_edit_name);
        mEtId = findViewById(R.id.et_course_edit_id);
        mEtTotalTime = findViewById(R.id.et_course_edit_time);
        mEtCredit = findViewById(R.id.et_course_edit_credit);
        mEtTeacher = findViewById(R.id.et_course_edit_teacher);
        mRbObligatory = findViewById(R.id.rb_course_edit_obligatory);
        mRbElective = findViewById(R.id.rb_course_edit_elective);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);
        findViewById(R.id.tv_course_edit_confirm).setOnClickListener(this);

        //id不可编辑
        mEtId.setEnabled(false);

        fillUI();
    }

    //用Course填充UI
    private void fillUI() {
        mTvTitle.setText("课程编辑");
        mEtName.setText(mCourse.getName());
        mEtId.setText(mCourse.getCourseId());
        mEtTotalTime.setText(String.valueOf(mCourse.getTotalTime()));
        mEtCredit.setText(String.valueOf(mCourse.getCredit()));
        mEtTeacher.setText(mCourse.getTeacher());
        if ("必修".equals(mCourse.getObligatory())) {
            mRbObligatory.setChecked(true);
            mRbElective.setChecked(false);
        } else {
            mRbObligatory.setChecked(false);
            mRbElective.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_course_edit_confirm:
                //确认修改
                DialogUtil.showDialog(this, "确认修改本课程？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit();
                    }
                });
                break;
        }
    }

    private void edit() {
        String name = mEtName.getText().toString();
        String id = mEtId.getText().toString();
        String totalTime = mEtTotalTime.getText().toString();
        String credit = mEtCredit.getText().toString();
        String teacher = mEtTeacher.getText().toString();
        String obligatory = mRbObligatory.isChecked() ? "必修" : "选修";

        //空检查
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(totalTime)
            || TextUtils.isEmpty(credit) || TextUtils.isEmpty(teacher) || TextUtils.isEmpty(obligatory)) {
            ToastUtil.showToast(this, "所有内容都不能为空！");
        }

        Course course = new Course();
        course.setName(name)
            .setCourseId(id)
            .setTotalTime(Integer.parseInt(totalTime))
            .setCredit(Float.parseFloat(credit))
            .setTeacher(teacher)
            .setObligatory(obligatory);
        if (Consts.IS_LOCAL) {
            //在本地数据库修改
            DBManager.getInstance().getCourseDao().updateData(mCourse.getCourseId(), course);
            EventBus.getDefault().post(new CourseChangedEvent(CourseChangedEvent.ACTION_EDIT, course));
            ToastUtil.showToast(MyApplication.getInstance(), "修改成功");
            finish();
        } else {
            // TODO: 2020/6/27
        }
    }
}