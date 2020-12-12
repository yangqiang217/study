package com.my.moduledemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.my.common_lib.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.printInfo();
    }
}