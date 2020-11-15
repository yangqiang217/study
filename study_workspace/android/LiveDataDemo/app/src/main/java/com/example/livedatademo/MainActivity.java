package com.example.livedatademo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.livedatademo.networklivedata.NetworkLiveData;

import java.util.Random;

/**
 * https://blog.csdn.net/gdutxiaoxu/article/details/86660760
 */
public class MainActivity extends AppCompatActivity {

    private String param = "param";

    private TextView tv;
    //todo 这里model完全可以删了，什么用？就是为了分离，类似于presenter
    private UserViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("shit", new Random().nextInt(100));
                model.getUserLiveData().setValue(user);
            }
        });

        findViewById(R.id.btn_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        model = ViewModelProviders.of(this, new UserViewModel.Factory(param)).get(UserViewModel.class);

        model.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.e("yqtest", "onChange: " + user.toString());
                tv.setText(user.toString());
            }
        });

        //network livedata
        NetworkLiveData.getInstance(this).observeForever(new Observer<NetworkInfo>() {
            @Override
            public void onChanged(NetworkInfo networkInfo) {
                Log.e("yqtest", "NetworkInfo onChange1: " + networkInfo);
            }
        });
    }
}