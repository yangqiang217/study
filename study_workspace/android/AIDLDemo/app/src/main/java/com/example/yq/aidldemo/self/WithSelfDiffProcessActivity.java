package com.example.yq.aidldemo.self;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yq.aidldemo.R;

public class WithSelfDiffProcessActivity extends AppCompatActivity {

    private TextView tv;
    private Button btn;

    private Self self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_self_diff_process);

        tv = findViewById(R.id.tv_self);
        btn = findViewById(R.id.btn_self);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println(self.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = new Intent();
        /* ********* 注意：：：：这里的ComponentName 第一个参数是应用的包名，而不是类的包名 *****************/
        intent.setComponent(new ComponentName("com.example.yq.aidldemo", "com.example.yq.aidldemo.self.SubService"));
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("onServiceConnected");
            self = Self.Stub.asInterface(service);

            try {
                tv.setText(self.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            self = null;
        }
    };
}
