package com.yq.lib_util;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * Created by yangqiang on 05/02/2018.
 */

public class InjectHelper {

    public static void inject(Activity host) {
        String classFullName = host.getClass().getName() + "$$ViewInjector";
        try {
            Class proxy = Class.forName(classFullName);
            Constructor constructor = proxy.getConstructor(host.getClass());
            constructor.newInstance(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
