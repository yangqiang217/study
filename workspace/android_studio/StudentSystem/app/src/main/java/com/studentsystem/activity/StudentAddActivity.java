package com.studentsystem.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Person;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.utils.BitmapUtil;
import com.studentsystem.utils.FileUtil;
import com.studentsystem.utils.ToastUtil;
import com.studentsystem.view.PersonEditView;

import org.greenrobot.eventbus.EventBus;

/**
 * 学生添加界面
 * 只有管理员能进来
 */
public class StudentAddActivity extends AppCompatActivity implements View.OnClickListener, PersonEditView.OnAvatarClickListener {

    private static final int REQUEST_CODE_GALLERY = 11;

    //导航title
    private TextView mTvTitle;
    private PersonEditView mViewPersonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);

        mTvTitle = findViewById(R.id.tv_nav_title);
        mViewPersonEdit = findViewById(R.id.v_stu_add_personedit);
        mViewPersonEdit.setIsStu(true);
        mViewPersonEdit.setOnAvatarClickListener(this);

        mTvTitle.setText("添加学生");

        findViewById(R.id.iv_nav_back).setOnClickListener(this);
        findViewById(R.id.tv_stu_add_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_back:
                finish();
                break;
            case R.id.tv_stu_add_confirm:
                //确认添加
                add();
                break;
        }
    }

    private void add() {
        if (!mViewPersonEdit.check()) {
            return;
        }

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
            //存在本地SQLite
            Student queryRes = DBManager.getInstance().getStudentDao().queryData(student.getStuId());
            if (queryRes != null && student.getStuId().equals(queryRes.getStuId())) {
                ToastUtil.showToast(this, "该学号的学生已经存在");
                return;
            }
            long res = DBManager.getInstance().getStudentDao().insertData(student);
            if (res >= 0) {
                ToastUtil.showToast(this, "学生添加成功");
                EventBus.getDefault().post(new StudentChangedEvent(StudentChangedEvent.ACTION_ADD, student));
                finish();
            } else {
                ToastUtil.showToast(this, "数据库写入失败");
            }

        } else {
            // TODO: 2020/6/25
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