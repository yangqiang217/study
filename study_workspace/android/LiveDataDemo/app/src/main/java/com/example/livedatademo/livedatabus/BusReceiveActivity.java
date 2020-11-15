package com.example.livedatademo.livedatabus;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.livedatademo.R;

public class BusReceiveActivity extends AppCompatActivity {

    public static final String LIVE_DATA_TAG = "livedatabus";
    public static final String LIVE_DATA_KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_bus);

        findViewById(R.id.btn_to_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusReceiveActivity.this, BusPostActivity.class));
            }
        });

        LiveDataBus.get().with(LIVE_DATA_KEY, TestEvent.class).observe(this, new Observer<TestEvent>() {
            @Override
            public void onChanged(@Nullable TestEvent event) {
                Log.d(LIVE_DATA_TAG, "observe: " + event.toString());
            }
        });

        LiveDataBus.get().with(LIVE_DATA_KEY, TestEvent.class).observeForever(new Observer<TestEvent>() {
            @Override
            public void onChanged(@Nullable TestEvent event) {
                Log.d(LIVE_DATA_TAG, "observeForever: " + event.toString());
            }
        });
    }
}