package com.studentsystem;

import android.app.Application;

import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.AdminChangedEvent;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.sp.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyApplication extends Application {

    private static MyApplication sMyApplication;

    /**
     * 当前登录的学生或管理员，登陆后保存在这里，sp里面也保存一份，下次打开app直接查询sp里是否有，有就直接按
     * 身份跳转到首页
     */
    private Student mLoginedStudent;
    private Admin mLoginedAdmin;

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        EventBus.getDefault().register(this);

        DBManager.getInstance().openDataBase();
        if (Consts.IS_LOCAL) {
            Consts.createStudent();
            Consts.createAdmin();
            Consts.createCourse();
        }

    }

    public static MyApplication getInstance() {
        return sMyApplication;
    }


    public boolean isLoginedStu() {
        return mLoginedStudent != null;
    }

    public Student getLoginedStudent() {
        if (mLoginedStudent == null) {
            mLoginedStudent = SpUtils.getLoginedStudent();
        }
        return mLoginedStudent;
    }
    public void setLoginedStudent(Student loginedStudent) {
        mLoginedStudent = loginedStudent;
        SpUtils.setLoginedStudent(loginedStudent);
    }
    public Admin getLoginedAdmin() {
        if (mLoginedAdmin == null) {
            mLoginedAdmin = SpUtils.getLoginedAdmin();
        }
        return mLoginedAdmin;
    }
    public void setLoginedAdmin(Admin loginedAdmin) {
        mLoginedAdmin = loginedAdmin;
        SpUtils.setLoginedAdmin(loginedAdmin);
    }

    /**
     * 接收eventbus事件，我的（学生）信息被修改后改变这儿存储的数据mLoginedStudent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStudentChangeEvent(StudentChangedEvent event) {
        if (event.getAction() == StudentChangedEvent.ACTION_EDIT
            && event.getStudent() != null
            && mLoginedStudent != null
            && event.getStudent().getStuId().equals(mLoginedStudent.getStuId())) {

            setLoginedStudent(event.getStudent());
        }
    }

    /**
     * 接收eventbus事件，我的（管理员）信息被修改后改变这儿存储的数据mLoginedAdmin
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveAdminChangeEvent(AdminChangedEvent event) {
        if (event.getAdmin() != null
            && mLoginedAdmin != null
            && event.getAdmin().getAdminId().equals(mLoginedAdmin.getAdminId())) {

            setLoginedAdmin(event.getAdmin());
        }
    }
}
