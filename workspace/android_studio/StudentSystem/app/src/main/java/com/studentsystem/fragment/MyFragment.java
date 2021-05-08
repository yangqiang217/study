package com.studentsystem.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.activity.CourseListActivity;
import com.studentsystem.activity.LoginActivity;
import com.studentsystem.activity.PersonDetailActivity;
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
 * "我的"标签
 * 学生有"我的成绩""我的课程表"两个按钮
 * 管理员有"我的授课"一个按钮
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    private TextView mTvName, mTvPhone, mTvEmail, mTvCollege, mTvMajor, mTvClass, mTvTotalCredit;
    private ImageView mIvAvatar;
    private ViewGroup mVgMajor, mVgClass, mVgMyCourse, mVgMyTeaching, mVgTotalCredit;

    //title
    private TextView mTvTitle;
    private ImageView mIvBack;

    private boolean mIsStu;
    private Student mLoginedStudent;
    private Admin mLoginedAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        mTvTitle = view.findViewById(R.id.tv_nav_title);
        mIvBack = view.findViewById(R.id.iv_nav_back);

        mTvName = view.findViewById(R.id.tv_my_name);
        mTvPhone = view.findViewById(R.id.tv_my_phone);
        mTvEmail = view.findViewById(R.id.tv_my_email);
        mTvCollege = view.findViewById(R.id.tv_my_college);
        mTvMajor = view.findViewById(R.id.tv_my_class);
        mTvClass = view.findViewById(R.id.tv_my_major);
        mTvTotalCredit = view.findViewById(R.id.tv_my_totalcredit);

        mIvAvatar = view.findViewById(R.id.iv_my_avatar);

        mVgMajor = view.findViewById(R.id.rl_my_major);//student only
        mVgClass = view.findViewById(R.id.rl_my_class);//student only
        mVgMyCourse = view.findViewById(R.id.rl_my_course);//student only
        mVgMyTeaching = view.findViewById(R.id.rl_my_teaching);//admin only
        mVgTotalCredit = view.findViewById(R.id.rl_my_totalcredit);//admin only

        mIsStu = MyApplication.getInstance().isLoginedStu();
        mLoginedStudent = MyApplication.getInstance().getLoginedStudent();
        mLoginedAdmin = MyApplication.getInstance().getLoginedAdmin();

        view.findViewById(R.id.rl_my_course).setOnClickListener(this);
        view.findViewById(R.id.rl_my_teaching).setOnClickListener(this);
        view.findViewById(R.id.tv_my_logout).setOnClickListener(this);

        view.findViewById(R.id.rl_my_name).setOnClickListener(this);
        view.findViewById(R.id.rl_my_avatar).setOnClickListener(this);
        view.findViewById(R.id.rl_my_phone).setOnClickListener(this);
        view.findViewById(R.id.rl_my_email).setOnClickListener(this);
        view.findViewById(R.id.rl_my_college).setOnClickListener(this);
        view.findViewById(R.id.rl_my_major).setOnClickListener(this);
        view.findViewById(R.id.rl_my_class).setOnClickListener(this);

        mTvTitle.setText("个人信息");
        mIvBack.setVisibility(View.GONE);

        fillUI();
        if (mIsStu) {
            getTotalCredit();
        }

        return view;
    }

    private void fillUI() {
        if (mIsStu) {
            mTvName.setText(mLoginedStudent.getName());
            mIvAvatar.setImageBitmap(BitmapUtil.bytes2Bitmap(mLoginedStudent.getAvatar()));
            mTvPhone.setText(mLoginedStudent.getPhone());
            mTvEmail.setText(mLoginedStudent.getEmail());
            mTvCollege.setText(mLoginedStudent.getCollege());
            mTvMajor.setText(mLoginedStudent.getMajor());
            mTvClass.setText(mLoginedStudent.getClassIn());

            mVgMajor.setVisibility(View.VISIBLE);
            mVgClass.setVisibility(View.VISIBLE);
            mVgTotalCredit.setVisibility(View.VISIBLE);
            mVgMyCourse.setVisibility(View.VISIBLE);
            mVgMyTeaching.setVisibility(View.GONE);
        } else {
            mTvPhone.setText(mLoginedAdmin.getPhone());
            mTvName.setText(mLoginedAdmin.getName());
            mIvAvatar.setImageBitmap(BitmapUtil.bytes2Bitmap(mLoginedAdmin.getAvatar()));
            mTvEmail.setText(mLoginedAdmin.getEmail());
            mTvCollege.setText(mLoginedAdmin.getCollege());

            mVgMajor.setVisibility(View.GONE);
            mVgClass.setVisibility(View.GONE);
            mVgTotalCredit.setVisibility(View.GONE);
            mVgMyCourse.setVisibility(View.GONE);
            mVgMyTeaching.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 在数据库或者网络上获取总学分
     */
    private void getTotalCredit() {
        if (Consts.IS_LOCAL) {
            List<StuCourse> myCourses = DBManager.getInstance().getStuCourseDao()
                .queryDataListByStuId(mLoginedStudent.getStuId());
            float total = 0;
            for (StuCourse stuCourse : myCourses) {
                total += stuCourse.getCredit();
            }
            mTvTotalCredit.setText(String.valueOf(total));
        } else {
            // TODO: 2020/6/30
        }
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStudentChangeEvent(StudentChangedEvent event) {
        //这个页面只关心个人信息修改
        if (event.getAction() == StudentChangedEvent.ACTION_EDIT
            && event.getStudent() != null
            && mLoginedStudent != null
            && event.getStudent().getStuId().equals(mLoginedStudent.getStuId())) {
            //表示修改的是我的信息
            mLoginedStudent = event.getStudent();
            fillUI();
        }
    }

    /**
     * 接收eventbus事件，学生信息被修改后改变列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveAdminChangeEvent(AdminChangedEvent event) {
        if (event.getAdmin() != null
            && mLoginedAdmin != null
            && event.getAdmin().getAdminId().equals(mLoginedAdmin.getAdminId())) {
            //表示修改的是我的信息
            mLoginedAdmin = event.getAdmin();
            fillUI();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_name:
            case R.id.rl_my_avatar:
            case R.id.rl_my_phone:
            case R.id.rl_my_email:
            case R.id.rl_my_college:
            case R.id.rl_my_major:
            case R.id.rl_my_class:
                //按当前登录的身份传对应的bean过去
                if (mLoginedStudent != null) {
                    PersonDetailActivity.start(getContext(), mLoginedStudent);
                } else if (mLoginedAdmin != null) {
                    PersonDetailActivity.start(getContext(), mLoginedAdmin);
                }
                break;
            case R.id.rl_my_course:
                gotoMyCourse();
                break;
            case R.id.rl_my_teaching:
                gotoMyTeaching();
                break;
            case R.id.tv_my_logout:
                DialogUtil.showDialog(getActivity(), "确认退出登录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().setLoginedStudent(null);
                        MyApplication.getInstance().setLoginedAdmin(null);
                        Intent intent4 = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent4);
                        getActivity().finish();
                    }
                });
                break;
        }
    }

    /**
     * 学生进入我选择的课程页面
     */
    private void gotoMyCourse() {
        if (Consts.IS_LOCAL) {
            //查询已选课程表
            List<StuCourse> stuCourseList = DBManager.getInstance().getStuCourseDao()
                .queryDataListByStuId(mLoginedStudent.getStuId());
            if (stuCourseList == null || stuCourseList.size() == 0) {
                ToastUtil.showToast(getContext(), "暂无已选课程");
                return;
            }
            //构造成dao可以用的形式，并计算总成绩
            List<String> courseIds = new ArrayList<>();
            float score = 0;
            for (StuCourse stuCourse : stuCourseList) {
                //1 给查数据库的list添加
                courseIds.add(stuCourse.getCourseId());
                //2 顺便计算成绩
                score += stuCourse.getScore();
                //3 顺便把key value对应信息整理放在这里好用
            }
            List<Course> myCourse = DBManager.getInstance().getCourseDao().queryDataListByIds(courseIds);
            CourseListActivity.start(getContext(), myCourse);
        } else {
            // TODO: 2020/6/27
        }
    }

    /**
     * 管理员进入我的授课页面
     */
    private void gotoMyTeaching() {
        if (Consts.IS_LOCAL) {
            List<Course> myTeachingList = DBManager.getInstance().getCourseDao()
                .queryDataListByTeacher(mLoginedAdmin.getName());
            if (myTeachingList == null || myTeachingList.size() == 0) {
                ToastUtil.showToast(getContext(), "您暂无授课");
            } else {
                CourseListActivity.start(getContext(), myTeachingList);
            }
        } else {
            // TODO: 2020/6/28
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}