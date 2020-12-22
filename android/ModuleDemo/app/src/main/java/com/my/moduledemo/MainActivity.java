package com.my.moduledemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.my.common_lib.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.printInfo();

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void shit(Event event) {

    }
}