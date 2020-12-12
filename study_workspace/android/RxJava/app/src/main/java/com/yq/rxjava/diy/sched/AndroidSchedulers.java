package com.yq.rxjava.diy.sched;

import android.os.Looper;

import com.yq.rxjava.diy.sched.scheduler.LooperScheduler;
import com.yq.rxjava.diy.sched.scheduler.Scheduler;

public class AndroidSchedulers {

    private final Scheduler mainThreadScheduler;

    public AndroidSchedulers() {
        this.mainThreadScheduler = new LooperScheduler(Looper.getMainLooper());
    }

    public static Scheduler mainThread() {
        return new AndroidSchedulers().mainThreadScheduler;
    }

}
