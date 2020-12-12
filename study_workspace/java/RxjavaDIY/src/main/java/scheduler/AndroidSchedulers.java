package scheduler;

import rx.Scheduler;

public class AndroidSchedulers {
    public static Scheduler mainThread() {
        return getInstance().mainThreadScheduler;
    }
}
