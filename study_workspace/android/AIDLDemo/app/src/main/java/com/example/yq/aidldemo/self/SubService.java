package com.example.yq.aidldemo.self;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by yq on 2017/10/20.
 */
public class SubService extends Service {

    private IBinder iBinder = new Self.Stub() {
        @Override
        public String getMessage() throws RemoteException {
            return "message";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return iBinder;
    }
}
