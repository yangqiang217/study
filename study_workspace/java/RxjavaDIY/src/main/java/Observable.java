import scheduler.Scheduler;

public class Observable<T> {

    final OnSubscribe<T> onSubscribe;

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        Observable<T> o = new Observable<T>(onSubscribe);
        return o;
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
        Observable<T> o = create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //切换到了子线程。Observable.this是create返回的，所以这里的call就是create创建时候自己写的call
                        Observable.this.onSubscribe.call(subscriber);
                    }
                });
            }
        });
        return o;
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        Observable<T> o = create(new OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {//subscriber就是外面subscribe的时候new出来的Subscriber todo
                subscriber.onStart();
                Scheduler.Worker worker = scheduler.createWorker();
                //Observable.this是subscribeOn返回的Observable，因为是subscribeOn返回的Observable调的observeOn,所以这里call的是subscribeOn里面那个call
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
        return o;
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }
}
