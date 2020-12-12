package com.example.yq.fragmentlifecycle;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.UUID;

public class MainActivity extends Activity {

    private Button btn1;
    private Button btn2;

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    private static int[] a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        a = new int[40 * 1024 * 1024];

        if (savedInstanceState != null) {
            firstFragment = (FirstFragment) getFragmentManager().findFragmentByTag(FirstFragment.TAG);
            secondFragment = (SecondFragment) getFragmentManager().findFragmentByTag(SecondFragment.TAG);
        } else {
            firstFragment = new FirstFragment();
            secondFragment = new SecondFragment();
            startFragment1();
        }

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        ClickListener clickListener = new ClickListener();
        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);

    }

    private void startFragment1() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        fragmentTransaction.add(R.id.container, firstFragment, FirstFragment.TAG).commit();
    }

    private void startFragment2() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        fragmentTransaction.replace(R.id.container, secondFragment, SecondFragment.TAG).addToBackStack(null).commit();
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.container, firstFragment, UUID.randomUUID().toString());
//                    fragmentTransaction.add(R.id.container, firstFragment);
                    fragmentTransaction.commit();
//                    fragmentTransaction.replace(R.id.container, firstFragment, FirstFragment.TAG).addToBackStack(null).commit();
                    break;
                case R.id.btn2:
                    startFragment2();
                    break;
                default:break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("activity onDestroy");
    }
}
