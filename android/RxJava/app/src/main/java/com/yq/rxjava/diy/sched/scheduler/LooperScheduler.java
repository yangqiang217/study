package com.yq.rxjava.diy.sched.scheduler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import rx.functions.Action0;

public class LooperScheduler extends Scheduler {

    private Handler mHandler;

    public LooperScheduler(Looper looper) {
        mHandler = new Handler(looper);
    }

    public LooperScheduler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public Worker createWorker() {
        return new HandlerWorker(mHandler);
    }

    static class HandlerWorker extends Scheduler.Worker {

        private Handler mHandler;

        public HandlerWorker(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void schedule(Action0 action) {
            ScheduledAction scheduledAction = new ScheduledAction(action, mHandler);

            Message message = Message.obtain(mHandler, scheduledAction);
            message.obj = this;

            mHandler.sendMessage(message);
        }
    }

    private static class ScheduledAction implements Runnable {
        private Action0 mAction;
        private Handler mHandler;

        public ScheduledAction(Action0 action, Handler handler) {
            mAction = action;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                mAction.call();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
