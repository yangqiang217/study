package com.example.locationmanagerhooker;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LocationManagerProxy implements InvocationHandler {

    private static final String TAG = "LocationManagerProxy";

    private Object mLocationManager;

    public LocationManagerProxy(Object locationManager) {
        mLocationManager = locationManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指的是mService的requestLocationUpdates方法，因为locationManager.requestLocationUpdates最终会调到mService里
        if (TextUtils.equals("requestLocationUpdates", method.getName())) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length < 3) {
                return null;
            }
            boolean foundLocationManager = false;
            for (int i = 0; i < stackTrace.length; i++) {
                StackTraceElement e = stackTrace[i];
                if (TextUtils.equals(e.getClassName(), "android.location.LocationManager")) {
                    foundLocationManager = true;
                    continue;
                }
                //找到LocationManager外层的调用者
                if (foundLocationManager && !TextUtils.equals(e.getClassName(), "android.location.LocationManager")) {
                    String invoker = e.getClassName() + "." + e.getMethodName();
                    Log.e(TAG, "invoker is " + invoker + "(" + args + ")");
                    break;
                }
            }
        }
        return method.invoke(mLocationManager, args);
    }
}
