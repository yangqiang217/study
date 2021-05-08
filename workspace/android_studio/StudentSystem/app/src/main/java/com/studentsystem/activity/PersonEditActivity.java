package com.studentsystem.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.studentsystem.MyApplication;
import com.studentsystem.R;
import com.studentsystem.bean.Admin;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.AdminChangedEvent;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.utils.BitmapUtil;
import com.studentsystem.utils.DialogUtil;
import com.studentsystem.utils.FileUtil;
import com.studentsystem.utils.ToastUtil;
import com.studentsystem.view.PersonEditView;

import org.greenrobot.eventbus.EventBus;

/**
 * 学生或者管理员信息修改界面，修改后用EventBus发送事件通知别的页面
 */
public class PersonEditActivity extends AppCompatActivity implements View.OnClickListener, PersonEditView.OnAvatarClickListener {

    //intent 传参用的key
    private static final String KEY_PARAM_STUDENT = "student";
    private static final String KEY_PARAM_ADMIN = "admin";

    private static final int REQUEST_CODE_GALLERY = 11;

    //导航title
    private TextView mTvTitle;
    private PersonEditView mViewPersonEdit;

    private boolean mIsEditingStu;
    private Student mStudent;
    private Admin mAdmin;

    //传student启动，显示student的信息
    public static void start(Context context, Student student) {
        Intent intent = new Intent(context, PersonEditActivity.class);
        intent.putExtra(KEY_PARAM_STUDENT, student);
        context.startActivity(intent);
    }

    //传admin启动，显示admin的信息
    public static void start(Context context, Admin admin) {
        Intent intent = new Intent(context, PersonEditActivity.class);
        intent.putExtra(KEY_PARAM_ADMIN, admin);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);

        mStudent = (Student) getIntent().getSerializableExtra(KEY_PARAM_STUDENT);
        mAdmin = (Admin) getIntent().getSerializableExtra(KEY_PARAM_ADMIN);
        mIsEditingStu = mStudent != null;

        mTvTitle = findViewById(R.id.tv_nav_title);

        mViewPersonEdit = findViewById(R.id.v_stu_edit_personedit);
        mViewPersonEdit.setIsStu(mIsEditingStu);
        mViewPersonEdit.setOnAvatarClickListener(this);

        findViewById(R.id.iv_nav_back).setOnClickListener(this);
        findViewById(R.id.tv_stu_edit_confirm).setOnClickListener(this);

        fillUI();
    }

    //用Student填充UI
    private void fillUI() {
        if (mIsEditingStu) {
            mTvTitle.setText("学生编辑");
            mViewPersonEdit.setPersonId(mStudent.getStuId());
            mViewPersonEdit.setPassword(mStudent.getPassword());
            mViewPersonEdit.setPasswordConfirm(mStudent.getPassword());
            mViewPersonEdit.setName(mStudent.getName());
            mViewPersonEdit.setAvatar(mStudent.getAvatar());
            mViewPersonEdit.setBirthday(mStudent.getBirthday());
            mViewPersonEdit.setSex(mStudent.getSex());
            mViewPersonEdit.setPhone(mStudent.getPhone());
            mViewPersonEdit.setEmail(mStudent.getEmail());
            mViewPersonEdit.setHometown(mStudent.getHometown());
            mViewPersonEdit.setCollege(mStudent.getCollege());
            mViewPersonEdit.setMajor(mStudent.getMajor());
            mViewPersonEdit.setClassIn(mStudent.getClassIn());
        } else {
            mTvTitle.setText("管理员编辑");
            mViewPersonEdit.setPersonId(mAdmin.getAdminId());
            mViewPersonEdit.setPassword(mAdmin.getPassword());
            mViewPersonEdit.setPasswordConfirm(mAdmin.getPassword());
            mViewPersonEdit.setName(mAdmin.getName());
            mViewPersonEdit.setAvatar(mAdmin.getAvatar());
            mViewPersonEdit.setBirthday(mAdmin.getBirthday());
            mViewPersonEdit.setSex(mAdmin.getSex());
            mViewPersonEdit.setPhone(mAdmin.getPhone());
            mViewPersonEdit.setEmail(mAdmin.getEmail());
            mViewPersonEdit.setHometown(mAdmin.getHometown());
            mViewPersonEdit.setCollege(mAdmin.getCollege());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_stu_edit_confirm:
                //确认修改
                DialogUtil.showDialog(this, "确认修改？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mViewPersonEdit.check()) {
                            return;
                        }
                        if (mIsEditingStu) {
                            editStudent();
                        } else {
                            editAdmin();
                        }
                    }
                });
                break;
        }
    }

    private void editStudent() {
        Student student = new Student();
        student.setName(mViewPersonEdit.getName())
            .setAvatar(mViewPersonEdit.getAvatar())
            .setStuId(mViewPersonEdit.getPersonId())
            .setPassword(mViewPersonEdit.getPassword())
            .setBirthday(mViewPersonEdit.getBirthday())
            .setSex(mViewPersonEdit.getSex())
            .setPhone(mViewPersonEdit.getPhone())
            .setEmail(mViewPersonEdit.getEmail())
            .setHometown(mViewPersonEdit.getHometown())
            .setCollege(mViewPersonEdit.getCollege())
            .setMajor(mViewPersonEdit.getMajor())
            .setClassIn(mViewPersonEdit.getClassIn());
        if (Consts.IS_LOCAL) {
            //在本地数据库修改
            long res = DBManager.getInstance().getStudentDao().updateData(mStudent.getStuId(), student);
            if (res >= 0) {
                ToastUtil.showToast(MyApplication.getInstance(), "修改成功");
                EventBus.getDefault().post(new StudentChangedEvent(StudentChangedEvent.ACTION_EDIT, student));
                finish();
            } else {
                ToastUtil.showToast(MyApplication.getInstance(), "数据库更新失败");
            }
        } else {
            // TODO: 2020/6/27
        }
    }

    private void editAdmin() {
        Admin admin = new Admin();
        admin.setName(mViewPersonEdit.getName())
            .setAvatar(mViewPersonEdit.getAvatar())
            .setAdminId(mViewPersonEdit.getPersonId())
            .setPassword(mViewPersonEdit.getPassword())
            .setBirthday(mViewPersonEdit.getBirthday())
            .setSex(mViewPersonEdit.getSex())
            .setPhone(mViewPersonEdit.getPhone())
            .setEmail(mViewPersonEdit.getEmail())
            .setHometown(mViewPersonEdit.getHometown())
            .setCollege(mViewPersonEdit.getCollege());
        if (Consts.IS_LOCAL) {
            //在本地数据库修改
            long res = DBManager.getInstance().getAdminDao().updateData(mAdmin.getAdminId(), admin);
            if (res >= 0) {
                EventBus.getDefault().post(new AdminChangedEvent().setAdmin(admin));
                ToastUtil.showToast(MyApplication.getInstance(), "修改成功");
                finish();
            } else {
                ToastUtil.showToast(MyApplication.getInstance(), "数据库更新失败");
            }
        } else {
            // TODO: 2020/6/27
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