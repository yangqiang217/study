package com.studentsystem.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.studentsystem.R;
import com.studentsystem.adapter.StudentListAdapter;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.listener.OnStudentItemClickListener;
import com.studentsystem.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

/**
 * 选择某个课程的学生列表页面
 */
public class StudentListActivity extends AppCompatActivity implements View.OnClickListener, OnStudentItemClickListener {

    private static final String KEY_PARAM_STU_LIST = "stu_list";
    private static final String KEY_PARAM_COURSE = "course";

    private TextView mTvTitle;
    private RecyclerView mRvStuList;
    private StudentListAdapter mStudentListAdapter;

    private List<Student> mStudentList;
    private Course mCourse;

    private AlertDialog mAssignScoreDialog;

    //跳转过去的位置，可能还会继续进编辑，记着位置编辑了好改变列表数据
    private int mJumpedPosition = -1;

    /**
     * @param studentList 数据量应该不会很大，所以用Intent传
     */
    public static void start(Context context, Course course, List<Student> studentList) {
        Intent intent = new Intent(context, StudentListActivity.class);
        intent.putExtra(KEY_PARAM_STU_LIST, (Serializable) studentList);
        intent.putExtra(KEY_PARAM_COURSE, course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        EventBus.getDefault().register(this);

        mStudentList = (List<Student>) getIntent().getSerializableExtra(KEY_PARAM_STU_LIST);
        mCourse = (Course) getIntent().getSerializableExtra(KEY_PARAM_COURSE);

        mTvTitle = findViewById(R.id.tv_nav_title);
        mRvStuList = findViewById(R.id.rv_stu_list);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);

        mTvTitle.setText("选择" + mCourse.getName() + "的学生");

        initRecyclerView();

        mStudentListAdapter.setDataList(mStudentList);
    }

    //初始化列表ui
    private void initRecyclerView() {
        mStudentListAdapter = new StudentListAdapter(this, true);
        mStudentListAdapter.setOnStudentItemClickListener(this);
        mRvStuList.setAdapter(mStudentListAdapter);
        mRvStuList.setLayoutManager(new LinearLayoutManager(this));
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
    public void onStudentItemClick(int position, Student student) {
        //管理员跳转学生编辑，需要知道编辑结果，会用EventBus通知
        mJumpedPosition = position;
        PersonDetailActivity.start(this, student);
    }

    @Override
    public void onAssignScoreClick(int position, final Student student) {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle("发布成绩")
            .setView(editText)
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            .setPositiveButton("发布", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        float res = Float.parseFloat(editText.getText().toString());
                        assignScore(res, student);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(StudentListActivity.this, "请输入数字");
                    }
                }
            });
        mAssignScoreDialog = builder.create();
        mAssignScoreDialog.show();
    }

    private void assignScore(float score, Student student) {
        if (Consts.IS_LOCAL) {
            StuCourse stuCourse = new StuCourse();
            stuCourse.setStuId(student.getStuId());
            stuCourse.setCourseId(mCourse.getCourseId());
            stuCourse.setScore(score);
            stuCourse.setCredit(StuCourse.getCreditByScore(score, mCourse.getCredit()));

            long res = DBManager.getInstance().getStuCourseDao().updateData(student.getStuId(), mCourse.getCourseId(), stuCourse);
            if (res >= 0) {
                ToastUtil.showToast(StudentListActivity.this, "发布成功");
                if (mAssignScoreDialog != null && mAssignScoreDialog.isShowing()) {
                    mAssignScoreDialog.dismiss();
                }
            } else {
                ToastUtil.showToast(StudentListActivity.this, "数据库写入失败");
            }
        } else {
            // TODO: 2020/6/30
        }
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStudentChangeEvent(StudentChangedEvent event) {
        switch (event.getAction()) {
            case StudentChangedEvent.ACTION_ADD:
                mStudentListAdapter.insertItem(event.getStudent());
                mRvStuList.scrollToPosition(0);
                break;
            case StudentChangedEvent.ACTION_EDIT:
                mStudentListAdapter.changeItem(mJumpedPosition, event.getStudent());
                break;
            case StudentChangedEvent.ACTION_DELETE:
                mStudentListAdapter.deleteItem(mJumpedPosition);
                break;
        }
        mJumpedPosition = -1;//修改了后还原
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须取消注册eventbus
        EventBus.getDefault().unregister(this);
    }
}