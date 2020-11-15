package com.example.yq.fragmentlifecycle;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SecondFragment extends Fragment {

    public static final String TAG = SecondFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("onAttach------>Fragment2");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated------>Fragment2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate------>Fragment2");

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("onCreateView------>Fragment2");

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart------>Fragment2");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume------>Fragment2");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("onHiddenChanged------>Fragment2");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause------>Fragment2");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop------>Fragment2");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("onDestroyView------>Fragment2");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy------>Fragment2");
        System.out.println("-------------------------------------");
    }
}
