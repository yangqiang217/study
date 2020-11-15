import thread.Scheduler;

public class Observable<T> {

    final OnSubscribe<T> onSubscribe;

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    /**
     * map的作用是什么？开辟一个新的Observable Ob2，替换subscriber（也就是main中真正new出来的最终结果那个订阅者）
     * 替换的目的就在于真正的订阅者订阅我的时候我在给它结果之前调func的call来进行相应的转换
     * @param func1
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Func1<? super T, ? extends R> func1) {
        return create(new OnSubscribeMap<T, R>(this, func1));
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Observable.this.onSubscribe.call(subscriber);
                    }
                });
            }
        });
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onComplete() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onComplete();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onNext(T t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(t);
                            }
                        });
                    }
                });
            }
        });
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }
}
