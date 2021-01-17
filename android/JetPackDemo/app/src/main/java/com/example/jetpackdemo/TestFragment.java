package com.example.jetpackdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jetpackdemo.databinding.FragmentTestBinding;

/**
 */
public class TestFragment extends Fragment {

    private FragmentTestBinding fragmentTestBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTestBinding = FragmentTestBinding.inflate(inflater, container, false);

//        TestViewModel testViewModel = new TestViewModelFactory(new TestRepository()).create(TestViewModel.class);
        //不是同一个
        MainViewModel testViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //和Activity的一样
        MainViewModel testViewModel2 = new ViewModelProvider(getActivity()).get(MainViewModel.class);
//        Log.d("yqtest", "onCreateView: " + testViewModel.hashCode() + ", " + testViewModel2.hashCode());

        fragmentTestBinding.tvFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTestBinding.tvFragment.setText("tv changed");
            }
        });

        return fragmentTestBinding.getRoot();
    }
}