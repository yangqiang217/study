package com.example.jetpackdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.jetpackdemo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private MainViewModel mMainViewModel;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mActivityMainBinding.getRoot());


        initViewModel();
        initClick();
        initFragment();
        initList();
    }

    private void initViewModel() {
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Log.d("yqtest", "onCreate: " + mMainViewModel.hashCode());

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
    }

    private void initClick() {
        mActivityMainBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainViewModel.start();
                //included
                mActivityMainBinding.included.tvIncludeTest.setText("include change");
            }
        });
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new TestFragment());
        fragmentTransaction.commit();
    }

    private void initList() {
        List<String> list = new ArrayList<>();
        mMainAdapter = new MainAdapter();
        mActivityMainBinding.rv.setAdapter(mMainAdapter);
        mActivityMainBinding.rv.setLayoutManager(new LinearLayoutManager(this));

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");

        mMainAdapter.setData(list);
        mMainAdapter.notifyDataSetChanged();
    }
}