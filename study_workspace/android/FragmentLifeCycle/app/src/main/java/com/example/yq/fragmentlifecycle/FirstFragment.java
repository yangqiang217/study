package com.example.yq.fragmentlifecycle;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstFragment extends Fragment {

    public static final String TAG = FirstFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("onAttach------>Fragment1");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated------>Fragment1");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate------>Fragment1");

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("onCreateView------>Fragment1");

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart------>Fragment1");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume------>Fragment1");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("onHiddenChanged------>Fragment1");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause------>Fragment1");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop------>Fragment1");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("onDestroyView------>Fragment1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy------>Fragment1");
        System.out.println("-------------------------------------");
    }
}
