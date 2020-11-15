package com.example.locationmanagerhooker;

import android.location.LocationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class HookHelper {

    /*
    主要原理就是把locationManager的mService用反射替换成代理fakeMService
    见MainActivity的change方法
     */
    public static void hookLocationManager(LocationManager locationManager) {
        try {
            Class<?> locationManagerClazz = Class.forName("android.location.LocationManager");
            Field field = locationManagerClazz.getDeclaredField("mService");
            field.setAccessible(true);
            Object oriMService = field.get(locationManager);

            Class<?> iLocationManagerClazz = Class.forName("android.location.ILocationManager");

            Object fakeMService = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iLocationManagerClazz}, new LocationManagerProxy(oriMService));

            Field field2 = locationManagerClazz.getDeclaredField("mService");
            field2.setAccessible(true);
            field2.set(locationManager, fakeMService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
