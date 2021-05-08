package com.example.yangqiang.test.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangqiang.test.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("fraglifecycle", "frag onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("fraglifecycle", "frag onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fraglifecycle", "frag onCreate");
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        Log.i("fraglifecycle", "frag onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("fraglifecycle", "frag onSaveInstanceState");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("fraglifecycle", "frag onViewCreated");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("fraglifecycle", "frag onViewStateRestored");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("fraglifecycle", "frag onConfigurationChanged");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("fraglifecycle", "frag onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fraglifecycle", "frag onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fraglifecycle", "frag onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fraglifecycle", "frag onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("fraglifecycle", "frag onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fraglifecycle", "frag onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("fraglifecycle", "frag onDetach");
    }
}