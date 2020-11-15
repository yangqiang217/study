package com.example.livedatademo;


import android.arch.lifecycle.Observer;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.livedatademo.networklivedata.NetworkLiveData;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //network livedata
        NetworkLiveData.getInstance(this).observe(this, new Observer<NetworkInfo>() {
            @Override
            public void onChanged(NetworkInfo networkInfo) {
                Log.e("yqtest", "NetworkInfo onChange2: " + networkInfo);
            }
        });
    }
}
