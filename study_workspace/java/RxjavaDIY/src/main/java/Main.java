import thread.Scheduler;
import thread.Schedulers;

/**
 * http://blog.csdn.net/tellh/article/details/71534704
 */
public class Main {
    public static void main(String[] args) {
        printWithThread("before");

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(111);
            }
        })
        .map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                printWithThread("map");
                return integer + "";
            }
        })
//        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                printWithThread(s);
            }
        });

        printWithThread("after");
    }

    private static void printWithThread(String content) {
        System.out.println(content + ", thread: " + Thread.currentThread().getName());
    }
}
