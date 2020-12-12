package com.yq.rxjava.diy.sched.scheduler;

import rx.functions.Action0;

public abstract class Scheduler {

    public abstract Worker createWorker();

    public abstract static class Worker {
        public abstract void schedule(Action0 action);
    }
}
