package com.my.common_lib;

import org.greenrobot.eventbus.EventBus;

public class Utils {
    public static void printInfo() {
        System.out.println("shit");
        EventBus.getDefault().register(new Utils());
    }
}
