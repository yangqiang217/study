package com.example.livedatademo.networklivedata;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkLiveData extends LiveData<NetworkInfo> {

    private static final String TAG = "NetworkLiveData";

    private final Context mContext;
    private static NetworkLiveData sInstance;
    private NetworkReceiver mNetworkReceiver;
    private final IntentFilter mIntentFilter;

    public NetworkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetworkLiveData getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkLiveData(context);
        }
        return sInstance;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d("yqtest", "onActive");
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    /**
     * 没人observe的时候调用
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("yqtest", "onInactive");
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            getInstance(context).setValue(activeNetwork);
        }
    }
}
