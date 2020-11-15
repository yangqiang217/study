package com.example.yq.handlerthread_intentservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntentServcieActivity extends Activity {

    public static final String UPLOAD_RESULT = "upload_result";

    private LinearLayout mLyTaskContainer;

    private BroadcastReceiver uploadImgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UPLOAD_RESULT.equals(intent.getAction())) {
                String path = intent.getStringExtra(UploadImgService.EXTRA_IMG_PATH);
                handleResult(path);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_servcie);

        mLyTaskContainer = (LinearLayout) findViewById(R.id.ll);

        registReceiver();
    }

    private void registReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPLOAD_RESULT);
        registerReceiver(uploadImgReceiver, intentFilter);
    }

    int i = 0;

    public void addTask(View view) {
        String path = (++i) + ".png";
        UploadImgService.startUploadImg(this, path);
        TextView tv = new TextView(this);
        mLyTaskContainer.addView(tv);
        tv.setText(path + " is uploading...");
        tv.setTag(path);
    }

    private void handleResult(String path) {
        TextView tv = (TextView) mLyTaskContainer.findViewWithTag(path);
        tv.setText(path + " upload success");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadImgReceiver);
    }
}
