package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.AdminChangedEvent;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.utils.BitmapUtil;
import com.studentsystem.utils.DialogUtil;
import com.studentsystem.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生或管理员个人信息详情页
 * 学生就传Student进来，管理员就传Admin进来
 * 可以点击编辑进个人信息编辑页面
 *
 * 某些View的可见是按照页面显示的是学生还是管理员决定的，比如专业班级成绩，
 * 某些View的可见是根据当前登录用户是学生还是管理员决定的，比如删除学生和查看选的课这俩按钮
 */
public class PersonDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //intent 传参用的key
    private static final String KEY_PARAM_STUDENT = "student";
    private static final String KEY_PARAM_ADMIN = "admin";

    //导航title
    private TextView mTvTitle;
    private TextView mTvName, mTvId, mTvBirthday, mTvSex, mTvPhone, mTvEmail, mTvHometown,
        mTvMyCollege, mTvMajor, mTvClass;
    private ImageView mIvAvatar;
    //button，mTvChosenCourse和mTvDeleteStu只有管理员可见
    private TextView mTvEdit, mTvChosenCourse, mTvDeleteStu;
    private ViewGroup mVgMajor, mVgClass;

    private boolean mIsLoginedStu;//当前登录身份
    private boolean mIsEditingStu;//当前编辑的信息的身份
    private Student mStudent;
    private Admin mAdmin;

    //传student启动，显示student的信息
    public static void start(Context context, Student student) {
        Intent intent = new Intent(context, PersonDetailActivity.class);
        intent.putExtra(KEY_PARAM_STUDENT, student);
        context.startActivity(intent);
    }

    //传admin启动，显示admin的信息
    public static void start(Context context, Admin admin) {
        Intent intent = new Intent(context, PersonDetailActivity.class);
        intent.putExtra(KEY_PARAM_ADMIN, admin);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        EventBus.getDefault().register(this);

        mStudent = (Student) getIntent().getSerializableExtra(KEY_PARAM_STUDENT);
        mAdmin = (Admin) getIntent().getSerializableExtra(KEY_PARAM_ADMIN);

        mIsLoginedStu = MyApplication.getInstance().isLoginedStu();
        mIsEditingStu = mStudent != null;

        mTvTitle = findViewById(R.id.tv_nav_title);

        mTvName = findViewById(R.id.tv_person_detail_name);
        mTvId = findViewById(R.id.tv_person_detail_id);
        mTvBirthday = findViewById(R.id.tv_person_detail_birthday);
        mTvSex = findViewById(R.id.tv_person_detail_sex);
        mTvPhone = findViewById(R.id.tv_person_detail_phone);
        mTvEmail = findViewById(R.id.tv_person_detail_email);
        mTvHometown = findViewById(R.id.tv_person_detail_hometown);
        mTvMyCollege = findViewById(R.id.tv_person_detail_college);
        mTvMajor = findViewById(R.id.tv_person_detail_major);
        mTvClass = findViewById(R.id.tv_person_detail_class);

        mIvAvatar = findViewById(R.id.iv_person_detail_avatar);

        mTvEdit = findViewById(R.id.tv_person_detail_edit);
        mTvChosenCourse = findViewById(R.id.tv_person_detail_chosen_course);
        mTvDeleteStu = findViewById(R.id.tv_person_detail_delete);

        mVgMajor = findViewById(R.id.rl_person_detail_major);
        mVgClass = findViewById(R.id.rl_person_detail_class);

        mTvEdit.setOnClickListener(this);
        mTvChosenCourse.setOnClickListener(this);
        mTvDeleteStu.setOnClickListener(this);
        findViewById(R.id.iv_nav_back).setOnClickListener(this);

        fillUI();

        mTvTitle.setText(mIsEditingStu ? "学生详情" : "管理员详情");

        //当前身份如果是admin，且页面信息是学生，可以看到更多按钮
        mTvChosenCourse.setVisibility(!mIsLoginedStu && mIsEditingStu ? View.VISIBLE : View.GONE);
        mTvDeleteStu.setVisibility(!mIsLoginedStu && mIsEditingStu ? View.VISIBLE : View.GONE);
    }

    /**
     * 用Student或者Admin填充UI
     */
    private void fillUI() {
        if (mIsEditingStu) {
            mVgMajor.setVisibility(View.VISIBLE);
            mVgClass.setVisibility(View.VISIBLE);

            mTvName.setText(mStudent.getName());
            mIvAvatar.setImageBitmap(BitmapUtil.bytes2Bitmap(mStudent.getAvatar()));
            mTvId.setText(mStudent.getStuId());
            mTvBirthday.setText(mStudent.getBirthday());
            mTvSex.setText(mStudent.getSex());
            mTvPhone.setText(mStudent.getPhone());
            mTvEmail.setText(mStudent.getEmail());
            mTvHometown.setText(mStudent.getHometown());
            mTvMyCollege.setText(mStudent.getCollege());
            mTvMajor.setText(mStudent.getMajor());
            mTvClass.setText(mStudent.getClassIn());
        } else {
            //是admin详情
            mVgMajor.setVisibility(View.GONE);
            mVgClass.setVisibility(View.GONE);

            mTvName.setText(mAdmin.getName());
            mIvAvatar.setImageBitmap(BitmapUtil.bytes2Bitmap(mAdmin.getAvatar()));
            mTvId.setText(mAdmin.getAdminId());
            mTvBirthday.setText(mAdmin.getBirthday());
            mTvSex.setText(mAdmin.getSex());
            mTvPhone.setText(mAdmin.getPhone());
            mTvEmail.setText(mAdmin.getEmail());
            mTvHometown.setText(mAdmin.getHometown());
            mTvMyCollege.setText(mAdmin.getCollege());
        }
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变这个页面数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStudentChangeEvent(StudentChangedEvent event) {
        mStudent = event.getStudent();
        fillUI();
    }
    /**
     * 接收eventbus事件，管理员信息被修改后改变这个页面数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveAdminChangeEvent(AdminChangedEvent event) {
        mAdmin = event.getAdmin();
        fillUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_person_detail_edit:
                if (mIsEditingStu) {
                    PersonEditActivity.start(this, mStudent);
                } else {
                    PersonEditActivity.start(this, mAdmin);
                }
                break;
            case R.id.tv_person_detail_chosen_course:
                //此学生选择的课程
                gotoChosenCourse();
                break;
            case R.id.tv_person_detail_delete:
                //删除此课程
                DialogUtil.showDialog(this, "确认删除此学生？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });
                break;
        }
    }

    /**
     * 进入学生选择的课程页面
     */
    private void gotoChosenCourse() {
        if (Consts.IS_LOCAL) {
//            //查询已选课程表
//            List<StuCourse> stuCourseList = DBManager.getInstance().getStuCourseDao()
//                .queryDataListByStuId(mStudent.getStuId());
//            if (stuCourseList == null || stuCourseList.size() == 0) {
//                ToastUtil.showToast(this, "暂无已选课程");
//                return;
//            }
//            //构造成dao可以用的形式，并计算总成绩
//            List<String> courseIds = new ArrayList<>();
//            float score = 0;
//            for (StuCourse stuCourse : stuCourseList) {
//                //1 给查数据库的list添加
//                courseIds.add(stuCourse.getCourseId());
//                //2 顺便计算成绩
//                score += stuCourse.getScore();
//                //3 顺便把key value对应信息整理放在这里好用
//            }
//            List<Course> myCourse = DBManager.getInstance().getCourseDao().queryDataListByIds(courseIds);
//            CourseListActivity.start(this, myCourse);

            CourseChooseActivity.start(this, mStudent);
        } else {
            // TODO: 2020/6/27
        }
    }

    private void delete() {
        if (Consts.IS_LOCAL) {
            //在本地数据库删除
            DBManager.getInstance().getStudentDao().deleteData(mStudent.getStuId());
            EventBus.getDefault().post(new StudentChangedEvent(StudentChangedEvent.ACTION_DELETE, mStudent));
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