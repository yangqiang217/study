package com.yq.rxjava.diy;

import com.yq.rxjava.diy.sched.scheduler.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class Observable<T> {

    /**
     * 类似于转接、电话员的角色，一有人调用subscribe，就拿到上一层的
     * onSubscribe（因为每一层的调用都是上一层新返回的Observable），
     * 调用call方法让上一层处理订阅者Subscriber。
     */
    final OnSubscribe<T> onSubscribe;

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        Observable<T> o = new Observable<T>(onSubscribe);
        return o;
    }

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    /**
     * 上面call我传上来了订阅者，我继续call我上面，call完给我结果了我再用func1里面的
     * 方法把结果转换一下，再拿转换后的结果通知我的订阅者
     */
    public <R> Observable<R> map(final Func1<? super T, ? extends R> func1) {
        //this 是ObservableC
//        OnSubscribe<R> onSubscribe = new OnSubscribeMap<>(this, func1);
//        return create(onSubscribe);

        Observable<R> o = create(new OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<? super R> subscriberMMid) {
                //this 是ObservableC
                /*类似这种在方法里面new OnSubscribe，然后在call里面调用的Observable.this
                都是上一层返回的Observable，因为调的是上一层的map方法
                */
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onComplete() {
                        subscriberMMid.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriberMMid.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        subscriberMMid.onNext(func1.call(t));
                    }
                });
            }
        });
        return o;
    }

    //以下map为方便测试，方便重命名
    public <R> Observable<R> map2(final Func1<? super T, ? extends R> func1) {
        Observable<R> o = create(new OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<? super R> subscriberOut) {
                //this 是ObservableB
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onComplete() {
                        subscriberOut.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriberOut.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        subscriberOut.onNext(func1.call(t));
                    }
                });
            }
        });
        return o;
    }
    public <R> Observable<R> mapMid(final Func1<? super T, ? extends R> func1) {
        Observable<R> o = create(new OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<? super R> subscriberB) {
                //this 是ObservableS,这里call的是subscribeOn里面那个call
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onComplete() {
                        subscriberB.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriberB.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        subscriberB.onNext(func1.call(t));
                    }
                });
            }
        });
        return o;
    }

    /**
     * 下面传给我订阅者Subscriber了，我不管三七二十一直接丢到我的线程里面，然后把下面的订阅者传给上面
     * 不能像observeOn那样保留一份订阅者，因为observeOn管的下面，保留是为了让下面在我的线程，
     * subscribeOn管的上面，所以就直接在子线程调用上层引用Observable.this.onSubscribe，这样导致
     * Observable.this.onSubscribe再调用上层引用都是在这个子线程。
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        Observable<T> o = create(new OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriberMMid) {
                subscriberMMid.onStart();

                scheduler.createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        //切换到了子线程。Observable.this是ObservableM，所以这里的call就是ObservableM的call
                        //为什么这里走的是ObservableM的call的，但是mapMid的func1里面也是子线程？
                        //因为：
                        Observable.this.onSubscribe.call(subscriberMMid);
                    }
                });
            }
        });
        return o;
    }

    /**
     * 我把下面的给我的订阅者即call传进来的Subscriber先保存在我这，因为调用上面的线程不可知，我重新建一份订阅者
     * 交给上面，等上面消息来了通知我了（即调用我的订阅方法了）我再把线程切换到我的线程，再去通知我的订阅者
     * 比如最后是在android主线程，那么上次调用我了我就用handler把上层给我的消息包装一下发到主线程，
     * 包装方式：新建ScheduledAction implements Runnable，这个Runnable就能放在Message里面（callback字段），
     * 然后Message被执行完会调用这个callback的run，我在run里面再调用我的Action0，就是A处new出来的
     */
    public Observable<T> observeOn(final Scheduler scheduler) {
        Observable<T> o = create(new OnSubscribe<T>() {

            @Override
            public void call(final Subscriber<? super T> subscriberM2) {
                subscriberM2.onStart();

                final Scheduler.Worker worker = scheduler.createWorker();

                Subscriber<T> subscriberB = new Subscriber<T>() {
                    @Override
                    public void onComplete() {
                        worker.schedule(new Action0() {//<----------A
                            @Override
                            public void call() {
                                subscriberM2.onComplete();
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable e) {
                        worker.schedule(new Action0() {
                            @Override
                            public void call() {
                                subscriberM2.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onNext(final T t) {
                        worker.schedule(new Action0() {
                            @Override
                            public void call() {
                                subscriberM2.onNext(t);
                            }
                        });
                    }
                };
                //Observable.this是mapMid返回的ObservableMMid，这里call的是mapMid里面那个call
                Observable.this.onSubscribe.call(subscriberB);
            }
        });
        return o;
    }

    public void subscribe(Subscriber<? super T> subscriberOut) {
        subscriberOut.onStart();
        onSubscribe.call(subscriberOut);
    }

    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
    }
}
