package com.example.jetpackdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.jetpackdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mActivityMainBinding.getRoot());

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        mMainViewModel.setOnTimeChangeListener(new TestViewModel.OnTimeChangeListener() {
//            @Override
//            public void onTimeChanged(int second) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv.setText(second + "");
//
//                    }
//                });
//            }
//        });

        mMainViewModel.getLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer second) {
                mActivityMainBinding.btnTest.setText(second + "");
            }
        });


        mActivityMainBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainViewModel.start();
                //included
                mActivityMainBinding.included.tvIncludeTest.setText("include change");
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new TestFragment());
        fragmentTransaction.commit();
    }


}