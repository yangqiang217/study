package com.studentsystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.studentsystem.R;
import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.utils.BitmapUtil;
import com.studentsystem.utils.FileUtil;
import com.studentsystem.utils.ToastUtil;
import com.studentsystem.view.PersonEditView;

/**
 * 注册页面，接受登录页面传过来的是否是学生，学生和管理员UI有点不一样
 * 注册给个默认头像
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, PersonEditView.OnAvatarClickListener{

    public static final String KEY_PARAM_STATUS = "status";
    private static final int REQUEST_CODE_GALLERY = 11;

    private TextView mTvTitle;
    private PersonEditView mViewPersonEdit;

    private boolean mIsStu;

    public static void start(Activity activity, boolean isStu) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(KEY_PARAM_STATUS, isStu);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mIsStu = getIntent().getBooleanExtra(KEY_PARAM_STATUS, true);

        mTvTitle = findViewById(R.id.tv_reg_title);

        mViewPersonEdit = findViewById(R.id.v_reg_personedit);

        findViewById(R.id.tv_reg_confirm).setOnClickListener(this);

        //.如果是管理员，隐藏专业和班级，顶部title文案不一样
        if (mIsStu) {
            mTvTitle.setText("学生注册");

        } else {
            mTvTitle.setText("管理员注册");
        }
        mViewPersonEdit.setIsStu(mIsStu);
        mViewPersonEdit.setOnAvatarClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reg_confirm:
                register();
                break;
        }
    }

    private void register() {
        if (!mViewPersonEdit.check()) {
            return;
        }

        String id = mViewPersonEdit.getPersonId();
        String psd = mViewPersonEdit.getPassword();
        String name = mViewPersonEdit.getName();
        byte[] avatar = mViewPersonEdit.getAvatar();
        String sex = mViewPersonEdit.getSex();
        String phone = mViewPersonEdit.getPhone();
        String email = mViewPersonEdit.getEmail();
        String hometown = mViewPersonEdit.getHometown();
        String birthday = mViewPersonEdit.getBirthday();
        String college = mViewPersonEdit.getCollege();
        String major = mViewPersonEdit.getMajor();
        String classIn = mViewPersonEdit.getClassIn();
        if (mIsStu) {
            Student student = new Student();
            student.setStuId(id)
                .setPassword(psd)
                .setName(name)
                .setAvatar(avatar)
                .setBirthday(birthday)
                .setSex(sex)
                .setPhone(phone)
                .setEmail(email)
                .setHometown(hometown)
                .setCollege(college)
                .setMajor(major)
                .setClassIn(classIn);

            stuRegister(student);
        } else {
            Admin admin = new Admin();
            admin.setAdminId(id)
                .setPassword(psd)
                .setName(name)
                .setAvatar(avatar)
                .setBirthday(birthday)
                .setSex(sex)
                .setPhone(phone)
                .setEmail(email)
                .setHometown(hometown)
                .setCollege(college);

            adminRegister(admin);
        }
    }

    private void stuRegister(Student student) {
        if (Consts.IS_LOCAL) {
            Student queryRes = DBManager.getInstance().getStudentDao().queryData(student.getStuId());
            if (queryRes != null) {
                ToastUtil.showToast(this, "学号已经存在，请登录");
                return;
            }
            long res = DBManager.getInstance().getStudentDao().insertData(student);
            if (res >= 0) {
                ToastUtil.showToast(this, "注册成功，请登录");
                finish();
            } else {
                ToastUtil.showToast(this, "数据库写入失败");
            }
        } else {

        }
    }

    private void adminRegister(Admin admin) {
        if (Consts.IS_LOCAL) {
            Admin queryRes = DBManager.getInstance().getAdminDao().queryData(admin.getAdminId());
            if (queryRes != null) {
                ToastUtil.showToast(this, "职工号已经存在，请登录");
                return;
            }
            long res = DBManager.getInstance().getAdminDao().insertData(admin);
            if (res >= 0) {
                ToastUtil.showToast(this, "注册成功，请登录");
                finish();
            } else {
                ToastUtil.showToast(this, "数据库写入失败");
            }
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);
                mViewPersonEdit.setAvatar(BitmapUtil.getBitmapFromFile(filePath));
                break;
        }
    }

    @Override
    public void onAvatarClick() {
        //检查图像权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        } else {
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, REQUEST_CODE_GALLERY);
    }
}