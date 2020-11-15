package com.yq.imageviewer.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yq.imageviewer.BuildConfig;
import com.yq.imageviewer.R;
import com.yq.imageviewer.event.BackPressEvent;
import com.yq.imageviewer.event.ClickDeleteEvent;
import com.yq.imageviewer.event.ClickMergeEvent;
import com.yq.imageviewer.event.EnterSelectModeEvent;
import com.yq.imageviewer.event.LastSelectUnCheckEvent;
import com.yq.imageviewer.event.LoadFinishEvent;
import com.yq.imageviewer.event.MainBottomAnimEvent;
import com.yq.imageviewer.fragment.FindFragment;
import com.yq.imageviewer.fragment.HomeFragment;
import com.yq.imageviewer.utils.MCountTimer;
import com.yq.imageviewer.utils.SecurityUtil;
import com.yq.imageviewer.utils.StatusBarUtil;
import com.yq.imageviewer.view.ScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SecurityUtil.onPermissionListener {

    private static final int BOTTOM_ANIM_DURATION = 300;

    @BindView(R.id.act_main_vp)
    ScrollViewPager mViewPager;
    @BindView(R.id.act_main_ll_bottom)
    View mLlBottom;
    @BindView(R.id.act_main_tv_home_count)
    TextView mTvCount;

    @BindView(R.id.act_main_select_mode_container)
    LinearLayout mLongPressTopContainer;

    private AlertDialog mLoginDialog;

    private float mBottomBarHeight;
    private boolean mBottomBarHide;

    private MCountTimer mMCountTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault()
            .register(this);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        mViewPager.setScrollable(false);

        mBottomBarHeight = getResources().getDimension(R.dimen.main_bottom_height);

        SecurityUtil.requestPermissions(this,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, this);
    }

    private void showPasswordDialog() {
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        final View dialogView = View.inflate(MainActivity.this, R.layout.dialog_password, null);
        // 获取布局中的控件
        final EditText etPsd = dialogView.findViewById(R.id.dlg_psd_et);
        final Button button = dialogView.findViewById(R.id.dlg_psd_btn);
        // 设置参数
        builder.setView(dialogView);
        builder.setCancelable(false);

        // 创建对话框
        mLoginDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkPassword(etPsd);
            }
        });
        mLoginDialog.show();

        etPsd.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkPassword(etPsd);
                    return true;
                }
                return false;
            }
        });

        etPsd.postDelayed(new Runnable() {
            @Override
            public void run() {
                etPsd.requestFocus();
                //调用系统输入法
                InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etPsd, 0);
            }
        }, 300);
    }

    private void checkPassword(EditText etPsd) {
        String psd = etPsd.getText()
            .toString()
            .trim();
        if (psd.equals("675725")) {
            loginSucc();
        } else {
            showToast("wrong password!");
            etPsd.setText("");
        }
    }

    private void prepareFingerprint() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        FingerprintManager fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager == null || !fingerprintManager.hasEnrolledFingerprints()) {
            return;
        }
        FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {}

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {}

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                loginSucc();
            }

            @Override
            public void onAuthenticationFailed() {}
        };
        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            showToast("没有指纹识别权限");
            return;
        }
        fingerprintManager.authenticate(null, new CancellationSignal(), 0, authenticationCallback, null);
    }

    private void loginSucc() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();// 对话框消失
        }
        mViewPager.setAdapter(new MAdapter(getSupportFragmentManager()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onBottomAnimEvent(MainBottomAnimEvent event) {
        if (event.isHide()) {
            hideBottomBar();
        } else {
            showBottomBar();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoadFinishEvent(LoadFinishEvent event) {
        mTvCount.setText(String.valueOf(event.getCount()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEnterSelectModeEvent(EnterSelectModeEvent event) {
        mLongPressTopContainer.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLastUnCheckEvent(LastSelectUnCheckEvent event) {
        mLongPressTopContainer.setVisibility(View.GONE);
    }

    private void showBottomBar() {
        if (!mBottomBarHide) {
            return;
        }
        mBottomBarHide = false;
        ObjectAnimator.ofFloat(mLlBottom, "translationY", mBottomBarHeight, 0)
            .setDuration(BOTTOM_ANIM_DURATION)
            .start();
    }

    private void hideBottomBar() {
        if (mBottomBarHide) {
            return;
        }
        mBottomBarHide = true;
        ObjectAnimator.ofFloat(mLlBottom, "translationY", 0, mBottomBarHeight)
            .setDuration(BOTTOM_ANIM_DURATION)
            .start();
    }

    @OnClick(R.id.act_main_iv_home)
    public void onHomeClick() {
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.act_main_iv_setting)
    public void onSettingClick() {
        mViewPager.setCurrentItem(1);
    }

    @OnClick(R.id.act_main_select_mode_merge)
    public void onMergeClick() {
        EventBus.getDefault().post(new ClickMergeEvent());
    }

    @OnClick(R.id.act_main_select_mode_delete)
    public void onDeleteClick() {
        EventBus.getDefault().post(new ClickDeleteEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault()
            .unregister(this);
    }

    @Override
    public boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        if (requestCode == SecurityUtil.ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                onGranted();
            } else {
                onRefused();
            }
        }
    }

    @Override
    public void onGranted() {
        prepareFingerprint();
        showPasswordDialog();
//        mViewPager.setAdapter(new MAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onRefused() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (HomeFragment.sSelectMode) {
            mLongPressTopContainer.setVisibility(View.GONE);
            EventBus.getDefault().post(new BackPressEvent());
            return;
        }
        if (mMCountTimer != null && mMCountTimer.isRunning()) {
            finish();
        }
        showToast("click again to exit~");
        mMCountTimer = new MCountTimer(2000, 1000);
        mMCountTimer.startTimer();
    }

    private static class MAdapter extends FragmentPagerAdapter {

        MAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new HomeFragment() : new FindFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
