package com.yq.rxjava.diy.sched;

import rx.functions.Action0;

public class ScheduledAction implements Runnable {

    Action0 action;

    public ScheduledAction(Action0 action) {
        this.action = action;
    }

    @Override
    public void run() {
        action.call();
    }
}
