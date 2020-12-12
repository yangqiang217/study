import scheduler.Schedulers;

/**
 * http://blog.csdn.net/tellh/article/details/71534704
 */
public class Main {
    public static void main(String[] args) {
        printWithThread("before");

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {//subscriber是observeOn里面call里new出来的 todo
                subscriber.onNext(111);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer s) {
                printWithThread(s);
            }
        });

        printWithThread("after");
    }

    private static void printWithThread(Object content) {
        System.out.println(content + ", thread: " + Thread.currentThread().getName());
    }
}
