package com.example.yq.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private IBinder mReporterBind;

    private class BindConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mReporterBind = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mReporterBind = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, BindService.class);
        bindService(intent, new BindConnection(), BIND_AUTO_CREATE);

        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken("reporter");
        data.writeString("this is a test string.");
        data.writeInt(type);

        mReporterBind.transact(BinderService.REPORT_CODE, data, reply, 0);
        reply.enforceInterface("reporter");
        int result = reply.readInt();

        data.recycle();
        reply.recycle();
    }
}
