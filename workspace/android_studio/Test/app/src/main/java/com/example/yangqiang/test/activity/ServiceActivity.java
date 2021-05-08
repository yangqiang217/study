package com.example.yangqiang.test.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yangqiang.test.R;
import com.example.yangqiang.test.service.MyService;

public class ServiceActivity extends AppCompatActivity {

    private MyService service;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MyService.MyBinder myBinder = (MyService.MyBinder)binder;
            service = myBinder.getService();
            int num = service.get();
            Log.i("lifecycle", "ServiceActivity onServiceConnected, getvalue: " + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("lifecycle", "ServiceActivity onServiceDisconnected");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        final Intent intent = new Intent(ServiceActivity.this, MyService.class);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startService(intent);
                bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopService(new Intent(ServiceActivity.this, MyService.class));
//                unbindService(conn);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceActivity.this, ThirdActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lifecycle", "ServiceActivity onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lifecycle", "ServiceActivity onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("lifecycle", "ServiceActivity onConfigurationChanged, newconfig: " + newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle", "ServiceActivity onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lifecycle", "ServiceActivity onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle", "ServiceActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle", "ServiceActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "ServiceActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "ServiceActivity onDestroy");
    }

}