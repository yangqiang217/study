package com.studentsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.utils.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText mEtId, mEtPsd;
    private TextView mTvConfirm, mTvRegisterStu, mTvRegisterAdmin;
    private RadioButton mRbStu, mRbAdmin;

    private boolean mIsStu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //如果已经登录了，直接跳转主页
        if (MyApplication.getInstance().getLoginedStudent() != null) {
            mIsStu = true;
            jumpToMain();
        } else if (MyApplication.getInstance().getLoginedAdmin() != null) {
            mIsStu = false;
            jumpToMain();
        }

        mEtId = findViewById(R.id.et_login_id);
        mEtPsd = findViewById(R.id.et_login_psd);
        mTvConfirm = findViewById(R.id.tv_login_confirm);
        mTvRegisterStu = findViewById(R.id.tv_login_register_stu);
        mTvRegisterAdmin = findViewById(R.id.tv_login_register_admin);
        mRbStu = findViewById(R.id.rb_login_stu);
        mRbAdmin = findViewById(R.id.rb_login_admin);

        mTvConfirm.setOnClickListener(this);
        mTvRegisterStu.setOnClickListener(this);
        mTvRegisterAdmin.setOnClickListener(this);
        mRbStu.setOnCheckedChangeListener(this);
        mRbAdmin.setOnCheckedChangeListener(this);

        mIsStu = mRbStu.isChecked();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_confirm:
                login();
                break;
            case R.id.tv_login_register_stu:
                RegisterActivity.start(this, true);
                break;
            case R.id.tv_login_register_admin:
                RegisterActivity.start(this, false);
                break;
        }

    }

    /**
     * 登录，先检查用户名和密码是否为空
     */
    private void login(){
        String id = mEtId.getText().toString();
        if (TextUtils.isEmpty(id)) {
            //学生和管理员提示内容不一样
            ToastUtil.showToast(this, (mIsStu ? "学号" : "职工号") + "不能为空");
            return;
        }
        String psd = mEtPsd.getText().toString();
        if (TextUtils.isEmpty(psd)) {
            ToastUtil.showToast(this, "密码不能为空");
            return;
        }

        if (Consts.IS_LOCAL) {
            //访问本地数据库，不请求网络
            if (mIsStu) {
                Student queryRes = DBManager.getInstance().getStudentDao().queryData(id);
                if (queryRes == null) {
                    ToastUtil.showToast(this, "用户不存在");
                    return;
                }
                if (!psd.equals(queryRes.getPassword())) {
                    ToastUtil.showToast(this, "密码错误");
                    return;
                }
                //成功登录，把登录用户设置给全局
                MyApplication.getInstance().setLoginedStudent(queryRes);
            } else {
                Admin queryRes = DBManager.getInstance().getAdminDao().queryData(id);
                if (queryRes == null) {
                    ToastUtil.showToast(this, "用户不存在");
                    return;
                }
                if (!psd.equals(queryRes.getPassword())) {
                    ToastUtil.showToast(this, "密码错误");
                    return;
                }
                //成功登录，把登录用户设置给全局
                MyApplication.getInstance().setLoginedAdmin(queryRes);
            }
            jumpToMain();
        } else {
            //发网络请求
        }
    }

    private void jumpToMain() {
        //验证成功，跳转主页，把当前身份记录到application中
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //防止登陆后在首页还能返回到登录页面
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_login_stu:
                    mIsStu = true;
                    mEtId.setHint("学号");
                    break;
                case R.id.rb_login_admin:
                    mIsStu = false;
                    mEtId.setHint("职工号");
                    break;
            }
        }
    }
}