package com.example.yangqiang.test.activity;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.yangqiang.test.R;
import com.example.yangqiang.test.fragment.FirstFragment;

public class FragActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        Log.i("fraglifecycle", "act onCreate");

        FirstFragment firstFragment = FirstFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_container, firstFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("fraglifecycle", "act onConfigurationChanged");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("fraglifecycle", "act onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("fraglifecycle", "act onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("fraglifecycle", "act onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("fraglifecycle", "act onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("fraglifecycle", "act onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("fraglifecycle", "act onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("fraglifecycle", "act onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("fraglifecycle", "act onDestroy");
    }
}