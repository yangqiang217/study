package com.yq.rxjava.diy.sched;

import com.yq.rxjava.diy.sched.scheduler.Scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import rx.functions.Action0;

public class NewThreadWorker extends Scheduler.Worker {

    private final ScheduledExecutorService executor;

    public NewThreadWorker() {
        executor = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void schedule(Action0 action) {
        scheduleActual(action);
    }

    public ScheduledAction scheduleActual(Action0 action) {
        ScheduledAction run = new ScheduledAction(action);
        Future<?> f = executor.submit(run);
        return run;
    }
}
