package com.yq.rxjava.diy.sched;

import com.yq.rxjava.diy.plugins.RxJavaSchedulersHook;
import com.yq.rxjava.diy.sched.scheduler.Scheduler;

public class Schedulers {

    private Scheduler ioScheduler;

    private static Schedulers sSchedulers;
    private static Schedulers getInstance() {
        synchronized (Schedulers.class) {
            if (sSchedulers == null) {
                sSchedulers = new Schedulers();
            }
        }
        return sSchedulers;
    }

    public Schedulers() {
        ioScheduler = RxJavaSchedulersHook.createIoScheduler();
    }

    public static Scheduler io() {
        return getInstance().ioScheduler;
    }
}
