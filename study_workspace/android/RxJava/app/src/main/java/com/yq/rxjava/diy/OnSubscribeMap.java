package com.yq.rxjava.diy;

import rx.functions.Func1;

public class OnSubscribeMap<T, R> implements Observable.OnSubscribe<R> {

    final Observable<T> source;
    final Func1<? super T, ? extends R> func1;

    public OnSubscribeMap(Observable<T> source, Func1<? super T, ? extends R> func1) {
        this.source = source;
        this.func1 = func1;
    }

    @Override
    public void call(final Subscriber<? super R> subscriberB) {
        //source是ObservableC
        source.subscribe(new MapSubscriber<>(subscriberB, func1));


        source.onSubscribe.call(new Subscriber<T>() {
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

    public static class MapSubscriber<T, R> extends Subscriber<R> {

        final Subscriber<? super T> actual;
        final Func1<? super R, ? extends T> func1;

        public MapSubscriber(Subscriber<? super T> subscriberB, Func1<? super R, ? extends T> func1) {
            this.actual = subscriberB;
            this.func1 = func1;
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }

        @Override
        public void onNext(R r) {
            actual.onNext(func1.call(r));
        }
    }
}
