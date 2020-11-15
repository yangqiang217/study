public class OnSubscribeMap<T, R> implements Observable.OnSubscribe {

    final Observable<T> source;
    final Func1<? super T, ? extends R> func1;

    public OnSubscribeMap(Observable<T> source, Func1<? super T, ? extends R> func1) {
        this.source = source;
        this.func1 = func1;
    }

    @Override
    public void call(Subscriber subscriber) {
        source.subscribe(new MapSubscriber<R, T>(subscriber, func1));
    }

    public static class MapSubscriber<T, R> extends Subscriber<R> {

        final Subscriber<? super T> actual;
        final Func1<? super R, ? extends T> func1;

        public MapSubscriber(Subscriber<? super T> actual, Func1<? super R, ? extends T> func1) {
            this.actual = actual;
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
