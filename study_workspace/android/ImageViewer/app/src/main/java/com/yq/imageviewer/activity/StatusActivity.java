package com.yq.imageviewer.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.R;
import com.yq.imageviewer.bean.PageItem;
import com.yq.imageviewer.event.RefreshEvent;
import com.yq.imageviewer.network.DownloadProgressCallback;
import com.yq.imageviewer.utils.FileUtil;
import com.yq.imageviewer.utils.ImageDownloadUtils;
import com.yq.imageviewer.utils.ImageUrlExtracter;
import com.yq.imageviewer.utils.des.DesUtil;
import com.yq.imageviewer.view.TitleAndDateView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatusActivity extends BaseActivity {

    private static final String KEY_PARAM_STATUS = "status";

    @BindView(R.id.act_status_titleanddate) TitleAndDateView mViewTitleAndDate;
    @BindView(R.id.act_status_et) EditText mEt;
    @BindView(R.id.act_status_bulletin) TextView mTvBulletin;
    @BindView(R.id.act_status_sv) ScrollView mSv;
    @BindView(R.id.act_status_btn_extract_input) Button mBtnSyncInput;

    private List<String> mLogList = new ArrayList<>();

    public static void start(Fragment fragment, int status) {
        Intent intent = new Intent(fragment.getContext(), StatusActivity.class);
        intent.putExtra(KEY_PARAM_STATUS, status);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int status = intent.getIntExtra(KEY_PARAM_STATUS, 0);

        switch (status) {
            case Status.SYNC_INPUT:
                mBtnSyncInput.setVisibility(View.VISIBLE);
                mViewTitleAndDate.setVisibility(View.VISIBLE);
                break;
            case Status.REALNAME_ENCRYPTEDNAME:
                showNameRelation();
                break;
            default: break;
        }
    }

    private void startSyncInput() {
        String url = mEt.getText().toString();
        if (TextUtils.isEmpty(url)) {
            showToast("please input some url");
            return;
        }
        String titleAndDate = mViewTitleAndDate.tryGetFormatedStr();
        if (!TextUtils.isEmpty(titleAndDate)) {
            startExtract(url, titleAndDate);
        }
    }

    private void showNameRelation() {
        mEt.setHint("input filter");
        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvBulletin.setText("");
                for (String log : mLogList) {
                    if (log.contains(s) || "".equals(s)) {
                        addBulletin(log, false);
                    }
                }
            }
        });
        File root = new File(Const.PATH);
        File[] pages = root.listFiles();
        if (pages == null || pages.length == 0) {
            showToast("can't find file");
            finish();
            return;
        }

        for (File page : pages) {
            String encrypted = page.getName();
            String original;
            try {
                original = DesUtil.decrypt(encrypted);
            } catch (Exception e) {
                e.printStackTrace();
                original = "**can't decrypt, msg: " + e.getMessage();
            }

            String log = original + " -> " + encrypted;
            mLogList.add(log);
            addBulletin(log, false);
        }
    }

    private void startExtract(String url, String titleAndTime) {
        ImageUrlExtracter.extractImageUrl(url, titleAndTime, new ImageUrlExtracter.OnExtractListener() {
            @Override
            public void onError(Throwable throwable) {
                addBulletin("extract error: " + throwable.getMessage(), true);
            }

            @Override
            public void onProgress(String msg) {
                addBulletin(msg, true);
            }

            @Override
            public void onSuccess(List<PageItem> imageUrls) {
                download(imageUrls);
            }
        });
    }

    private void download(List<PageItem> pages) {
        ImageDownloadUtils.download(pages, new DownloadProgressCallback() {

            @Override
            public void onSuccess() {
                addBulletin("download finish!", true);
                EventBus.getDefault().post(new RefreshEvent());
            }

            @Override
            public void onProgress(String msg) {
                addBulletin(msg, true);
            }

            @Override
            public void onError(String msg) {
                addBulletin("download error: " + msg, true);
            }
        });
    }

    private void addBulletin(final String msg, final boolean scrollToEnd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvBulletin.append("\n");
                mTvBulletin.append(msg);
                if (scrollToEnd) {
                    mSv.post(new Runnable() {
                        @Override
                        public void run() {
                            mSv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.act_status_btn_extract_input)
    public void onExtractFileClick() {
        startSyncInput();
    }

    public static final class Status {
        /**
         * 带et,btn和log
         */
        public static final int SYNC_INPUT = 0;
        /**
         * et,log
         */
        public static final int REALNAME_ENCRYPTEDNAME = 1;
    }
}
