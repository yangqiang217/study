package com.example.livedatademo.livedatabus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.livedatademo.R;

import static com.example.livedatademo.livedatabus.BusReceiveActivity.LIVE_DATA_TAG;

public class BusPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_post);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LIVE_DATA_TAG, "bus send");
                LiveDataBus.get().with(BusReceiveActivity.LIVE_DATA_KEY, TestEvent.class).post(new TestEvent("yq post"));
            }
        });
    }
}