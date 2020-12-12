package com.yq.rxjava.diy.sched.scheduler;

import com.yq.rxjava.diy.sched.NewThreadWorker;
import com.yq.rxjava.diy.sched.ScheduledAction;

import rx.functions.Action0;

public class CachedThreadScheduler extends Scheduler {
    @Override
    public Worker createWorker() {
        return new EventLoopWorker();
    }

    static class EventLoopWorker extends Worker implements Action0 {
        private final ThreadWorker threadWorker;

        public EventLoopWorker() {
            this.threadWorker = new ThreadWorker();
        }

        @Override
        public void call() {

        }

        @Override
        public void schedule(final Action0 action) {
            ScheduledAction s = threadWorker.scheduleActual(new Action0() {
                @Override
                public void call() {
                    action.call();
                }
            });
        }
    }

    static class ThreadWorker extends NewThreadWorker {

    }
}
