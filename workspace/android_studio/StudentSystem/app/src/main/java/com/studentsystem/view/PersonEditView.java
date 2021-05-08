package com.studentsystem.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.studentsystem.R;
import com.studentsystem.utils.BitmapUtil;
import com.studentsystem.utils.ToastUtil;

import java.util.Calendar;

/**
 * 学生、管理员信息编辑、注册view
 * 公用的view和代码比较多，所以封装成一个view
 */
public class PersonEditView extends FrameLayout implements View.OnClickListener {


    private Context mContext;
    private EditText mEtName, mEtId, mEtPsd, mEtPsdConfirm, mEtPhone, mEtEmail,
        mEtHometown, mEtCollege, mEtMajor, mEtClass;
    private TextView mTvIdTitle, mTvBirthday;
    private TextView mTvTitleMajor, mTvTitleClass;
    private RadioButton mRbMale, mRbFemale;
    private ImageView mIvAvatar;

    private OnAvatarClickListener mOnAvatarClickListener;

    //管理员不显示专业、班级
    private boolean mIsStu;

    public PersonEditView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PersonEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.view_person_edit, this);

        mEtName = findViewById(R.id.et_person_edit_name);
        mEtId = findViewById(R.id.et_person_edit_id);
        mEtPsd = findViewById(R.id.et_person_edit_psd);
        mEtPsdConfirm = findViewById(R.id.et_person_edit_psd_confirm);
        mEtPhone = findViewById(R.id.et_person_edit_phone);
        mEtEmail = findViewById(R.id.et_person_edit_email);
        mEtHometown = findViewById(R.id.et_person_edit_hometown);
        mEtCollege = findViewById(R.id.et_person_edit_college);
        mEtMajor = findViewById(R.id.et_person_edit_major);
        mEtClass = findViewById(R.id.et_person_edit_class);

        mTvIdTitle = findViewById(R.id.tv_person_edit_id);
        mTvBirthday = findViewById(R.id.tv_person_edit_birthday);

        mTvTitleMajor = findViewById(R.id.tv_person_edit_major);
        mTvTitleClass = findViewById(R.id.tv_person_edit_class);

        mRbMale = findViewById(R.id.rb_person_edit_male);
        mRbFemale = findViewById(R.id.rb_person_edit_female);

        mIvAvatar = findViewById(R.id.iv_person_edit_avatar);

        mTvBirthday.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
    }

    public void setIsStu(boolean isStu) {
        mIsStu = isStu;
        mEtMajor.setVisibility(isStu ? VISIBLE : GONE);
        mEtClass.setVisibility(isStu ? VISIBLE : GONE);
        mTvTitleMajor.setVisibility(isStu ? VISIBLE : GONE);
        mTvTitleClass.setVisibility(isStu ? VISIBLE : GONE);

        mTvIdTitle.setText(isStu ? "学号" : "职工号");
    }

    public void showDatePickDialog(DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();

        int year = 0, month = 0, day = 0;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 检查各个字段
     */
    public boolean check() {
        if (TextUtils.isEmpty(getPersonId())) {
            ToastUtil.showToast(mContext, (mIsStu ? "学号" : "职工号") + "不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getPassword())) {
            ToastUtil.showToast(mContext, "密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getPasswordConfirm())) {
            ToastUtil.showToast(mContext, "确认密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getName())) {
            ToastUtil.showToast(mContext, "姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getPhone())) {
            ToastUtil.showToast(mContext, "手机号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getHometown())) {
            ToastUtil.showToast(mContext, "籍贯不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getBirthday())) {
            ToastUtil.showToast(mContext, "出生日期不能为空");
            return false;
        }
        if (TextUtils.isEmpty(getCollege())) {
            ToastUtil.showToast(mContext, "学院姓名不能为空");
            return false;
        }
        if (mIsStu) {
            //学生还需要检查专业和班级
            if (TextUtils.isEmpty(getMajor())) {
                ToastUtil.showToast(mContext, "专业不能为空");
                return false;
            }
            if (TextUtils.isEmpty(getClassIn())) {
                ToastUtil.showToast(mContext, "班级不能为空");
                return false;
            }
        }

        //空检查通过，密码检查
        if (!getPassword().equals(getPasswordConfirm())) {
            ToastUtil.showToast(mContext, "两次密码不一致");
            return false;
        }

        return true;
    }

    public String getPersonId() {
        return mEtId.getText().toString().trim();
    }
    public void setPersonId(String id) {
        mEtId.setText(id);
        //只要有设置过，id就是不可编辑的
        mEtId.setEnabled(false);
    }
    public String getName() {
        return mEtName.getText().toString().trim();
    }
    public void setName(String name) {
        mEtName.setText(name);
    }
    public byte[] getAvatar() {
        return BitmapUtil.bitmap2Bytes(((BitmapDrawable)mIvAvatar.getDrawable()).getBitmap());
    }
    public void setAvatar(byte[] avatar) {
        mIvAvatar.setImageBitmap(BitmapUtil.bytes2Bitmap(avatar));
    }
    public String getPassword() {
        return mEtPsd.getText().toString().trim();
    }
    public void setPassword(String psd) {
        mEtPsd.setText(psd);
    }
    public String getPasswordConfirm() {
        return mEtPsdConfirm.getText().toString().trim();
    }
    public void setPasswordConfirm(String psdConfirm) {
        mEtPsdConfirm.setText(psdConfirm);
    }
    public String getSex() {
        return mRbMale.isChecked() ? "男" : "女";
    }
    public void setSex(String sex) {
        if ("男".equals(sex)) {
            mRbMale.setChecked(true);
            mRbFemale.setChecked(false);
        } else {
            mRbMale.setChecked(false);
            mRbFemale.setChecked(true);
        }
    }
    public String getBirthday() {
        return mTvBirthday.getText().toString().trim();
    }
    public void setBirthday(String birthday) {
        mTvBirthday.setText(birthday);
    }
    public String getPhone() {
        return mEtPhone.getText().toString().trim();
    }
    public void setPhone(String phone) {
        mEtPhone.setText(phone);
    }
    public String getEmail() {
        return mEtEmail.getText().toString().trim();
    }
    public void setEmail(String email) {
        mEtEmail.setText(email);
    }
    public String getHometown() {
        return mEtHometown.getText().toString().trim();
    }
    public void setHometown(String hometown) {
        mEtHometown.setText(hometown);
    }
    public String getCollege() {
        return mEtCollege.getText().toString().trim();
    }
    public void setCollege(String college) {
        mEtCollege.setText(college);
    }
    public String getMajor() {
        return mEtMajor.getText().toString().trim();
    }
    public void setMajor(String major) {
        mEtMajor.setText(major);
    }
    public String getClassIn() {
        return mEtClass.getText().toString().trim();
    }
    public void setClassIn(String classIn) {
        mEtClass.setText(classIn);
    }

    public void setOnAvatarClickListener(OnAvatarClickListener onAvatarClickListener) {
        mOnAvatarClickListener = onAvatarClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_person_edit_birthday:
                showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;//系统得到的月份是0-11
                        String date = year + "-" + month + "-" + dayOfMonth;
                        mTvBirthday.setText(date);
                    }
                });
                break;
            case R.id.iv_person_edit_avatar:
                if (mOnAvatarClickListener != null) {
                    mOnAvatarClickListener.onAvatarClick();
                }
                break;
        }
    }

    //把点击头像的事件回调出去，因为在view里面无法获取选择回来的图片
    public interface OnAvatarClickListener {
        void onAvatarClick();
    }
}
