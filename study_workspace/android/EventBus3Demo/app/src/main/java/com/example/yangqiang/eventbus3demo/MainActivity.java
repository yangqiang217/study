package com.example.yangqiang.eventbus3demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {


    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.fl);

        getSupportFragmentManager().beginTransaction().add(R.id.fl, new InnerFragment()).commit();
    }

    //yes
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rec1(TransData data) {
        try {
            String a = null;
            System.out.println(a.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("rec1: " + data.toString());
    }

    //yes
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rec2(TransData data) {
        System.out.println("rec2: " + data.toString());
    }

    //no
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rec3() {
        System.out.println("rec3: ");
    }

    //no
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rec4(TransData data, int a) {
        System.out.println("rec4: " + data.toString());
    }
}
