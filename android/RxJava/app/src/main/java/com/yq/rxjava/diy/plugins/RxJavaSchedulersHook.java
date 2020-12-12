package com.yq.rxjava.diy.plugins;

import com.yq.rxjava.diy.sched.scheduler.CachedThreadScheduler;
import com.yq.rxjava.diy.sched.scheduler.Scheduler;

public class RxJavaSchedulersHook {

    public static Scheduler createIoScheduler() {
        return new CachedThreadScheduler();
    }
}
