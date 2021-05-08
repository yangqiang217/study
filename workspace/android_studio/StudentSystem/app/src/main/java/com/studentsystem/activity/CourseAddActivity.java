package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.CourseChangedEvent;
import com.studentsystem.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 课程添加页面
 * 只有管理员才能进来
 */
public class CourseAddActivity extends AppCompatActivity implements View.OnClickListener {

    //导航title
    private TextView mTvTitle;
    private EditText mEtName, mEtId, mEtTotalTime, mEtCredit, mEtTeacher;
    private RadioButton mRbObligatory, mRbElective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        mTvTitle = findViewById(R.id.tv_nav_title);

        mEtName = findViewById(R.id.et_course_edit_name);
        mEtId = findViewById(R.id.et_course_edit_id);
        mEtTotalTime = findViewById(R.id.et_course_edit_time);
        mEtCredit = findViewById(R.id.et_course_edit_credit);
        mEtTeacher = findViewById(R.id.et_course_edit_teacher);
        mRbObligatory = findViewById(R.id.rb_course_edit_obligatory);
        mRbElective = findViewById(R.id.rb_course_edit_elective);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);
        findViewById(R.id.tv_course_add_confirm).setOnClickListener(this);

        mTvTitle.setText("课程添加");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_course_add_confirm:
                //确认添加
                add();
                break;
        }
    }

    private void add() {
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
            //存在本地SQLite
            Course queryRes = DBManager.getInstance().getCourseDao().queryData(id);
            if (queryRes != null && id.equals(queryRes.getCourseId())) {
                ToastUtil.showToast(this, "该编号的课程已经存在");
                return;
            }
            long res = DBManager.getInstance().getCourseDao().insertData(course);
            if (res >= 0) {
                ToastUtil.showToast(this, "课程添加成功");
                EventBus.getDefault().post(new CourseChangedEvent(CourseChangedEvent.ACTION_ADD, course));
                finish();
            } else {
                ToastUtil.showToast(this, "数据库写入失败");
            }

        }
        // TODO: 2020/6/25
    }
}