package com.yq.imageviewer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleAndDateView extends FrameLayout {

    @BindView(R.id.et_title) EditText mEtInputTitle;
    @BindView(R.id.et_year) EditText mEtInputYear;
    @BindView(R.id.et_month) EditText mEtInputMonth;
    @BindView(R.id.et_day) EditText mEtInputDay;

    private Context mContext;

    public TitleAndDateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TitleAndDateView(@NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.viewgroup_title_and_time, this);
        ButterKnife.bind(this);

        mContext = context;

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        String year = c.get(Calendar.YEAR) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        month = (month.length() == 1 ? "0" + month : month);

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        day = (day.length() == 1 ? "0" + day : day);

        mEtInputYear.setText(year);
        mEtInputMonth.setText(month);
        mEtInputDay.setText(day);
    }

    public String tryGetFormatedStr() {
        String title = mEtInputTitle.getText().toString();
        String year = mEtInputYear.getText().toString();
        String month = mEtInputMonth.getText().toString();
        String day = mEtInputDay.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(year)
            || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
            showToast("content can't be null");
            return null;
        }

        if (year.length() != 4) {
            showToast("year length must be 4");
            return null;
        }
        if (month.length() != 2) {
            showToast("month length must be 2");
            return null;
        }
        if (day.length() != 2) {
            showToast("day length must be 2");
            return null;
        }
        return title + Const.GAP_TITLE_TIME
            + year + Const.GAP_TIME + month + Const.GAP_TIME + day;
    }

    protected final void showToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }
}
