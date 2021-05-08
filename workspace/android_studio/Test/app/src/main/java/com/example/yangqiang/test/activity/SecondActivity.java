package com.example.yangqiang.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.yangqiang.test.MyApplication;
import com.example.yangqiang.test.R;
import com.example.yangqiang.test.service.MyService;
import com.example.yangqiang.test.utils.FilePath;

public class SecondActivity extends AppCompatActivity {


    private static final String TAG = "SecondActivity";
    FilePath mFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.i("lifecycle", "second onCreate");

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().start();

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lifecycle", "second onNewIntent");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lifecycle", "second onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle", "second onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle", "second onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            Log.i("lifecycle", "second onPause, isfinishing");
        }
        Log.i("lifecycle", "second onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "second onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "second onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("startmode", "second onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);
    }
}
