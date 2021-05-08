package com.example.yangqiang.test.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.yangqiang.test.MyApplication;
import com.example.yangqiang.test.R;
import com.example.yangqiang.test.service.MyService;
import com.example.yangqiang.test.test.Bean;
import com.example.yangqiang.test.test.WaitNotify;
import com.example.yangqiang.test.utils.BitmapCompress;
import com.example.yangqiang.test.utils.FilePath;
import com.example.yangqiang.test.utils.Lock;
import com.example.yangqiang.test.utils.ToastUtils;
import com.example.yangqiang.test.utils.ViewUtils;
import com.example.yangqiang.test.view.MyView;
import com.google.gson.Gson;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PATH = "/sdcard/sizefile.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("lifecycle", "main onCreate");

//        ActivityCompat.requestPermissions(MainActivity.this,
//            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//            1001);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("btn1 click");
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                findViewById(R.id.btn).performClick();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("bitmapsize", "onRequestPermissionsResult: PERMISSION_GRANTED");

                } else {
                    Log.d("bitmapsize", "onRequestPermissionsResult: PERMISSION_not GRANTED");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("lifecycle", "main onWindowFocusChanged, hasFocus: " + hasFocus);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lifecycle", "main onNewIntent, intent: " + intent.hashCode());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lifecycle", "main onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lifecycle", "main onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("lifecycle", "main onConfigurationChanged, newconfig: " + newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle", "main onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lifecycle", "main onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle", "main onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle", "main onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "main onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "main onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("startmode", "main onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);
    }
}
