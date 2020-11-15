package com.yq.rxjava;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * http://www.jianshu.com/p/5e93c9101dc5
 * http://www.jianshu.com/p/240f1c8ebf9d
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Observer<String> stringReceiver;
    private Observer<Long> longReceiver;
    private Action1<Integer> intReceiver;

    static class AA {
        List<String> list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnToSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(String.valueOf(i));
        }
        strings.add(null);
        Observable.from(strings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s.length());
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onnext: " + s + " onthread: " + Thread.currentThread().getName());
                    }
                });


        initObserver();

        //1 create方式
        Observable<String> sender = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hi, weavy1 on thread: " + Thread.currentThread().getName());
                subscriber.onNext("hi, weavy2");
                subscriber.onCompleted();
            }
        });

        //2 just方式 将为你创建一个Observable并自动为你调用onNext( )发射数据
        Observable<String> justObservable = Observable.just("just1", "just2");

        //3 from list 遍历集合，发送每个item
        List<String> list = new ArrayList<String>();
        list.add("from1");
        list.add("from2");
        list.add("from3");
        Observable<String> fromObservable = Observable.from(list);

        //4 defer 有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
        Observable<String> deferObservable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("defer1", "defer2");
            }
        });

        //5 interval 按固定时间间隔发射整数序列的Observable，可用作定时器
        Observable<Long> intervalObservable = Observable.interval(1, TimeUnit.SECONDS);//send per second

        //6 range 创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，如果为0则不发送，负数则抛异常
        Observable<Integer> rangeObservable = Observable.range(10, 5);

        //7 timer 创建一个Observable，它在一个给定的延迟后发射一个特殊的值，等同于Android中Handler的postDelay( )方法
        Observable<Long> timerObservable = Observable.timer(10, TimeUnit.SECONDS);

        //8 repeat 创建一个重复发射特定数据的Observable
        Observable<String> repeatObservable = Observable.just("repeat1", "repeat2").repeat(3);

        sender.subscribe(stringReceiver);//一订阅完就会走receiver的东西
//        justObservable.subscribe(stringReceiver);
//        fromObservable.subscribe(stringReceiver);
//        deferObservable.subscribe(stringReceiver);
//        intervalObservable.subscribe(longReceiver);
//        rangeObservable.subscribe(intReceiver);
//        timerObservable.subscribe(longReceiver);
//        repeatObservable.subscribe(stringReceiver);

        //subjet的
//        asyncSubject();
//        behaviorSubject();
//        publishSubject();
//        replaySubject();
    }

    /**
     * Subject之　AsyncSubject
     * Observer会接收AsyncSubject的`onComplete()之前的最后一个数据
     * 如果不掉用onCompleted，那么observer毛都收不到
     */
    private void asyncSubject() {
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("asyncSubject1");
        asyncSubject.onNext("asyncSubject2");
        asyncSubject.onNext("asyncSubject3");
        asyncSubject.onCompleted();
//        asyncSubject.onError(new Throwable("err async"));
        asyncSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log("asyncSubject completed");
            }

            @Override
            public void onError(Throwable e) {
                Log("asyncSubject onError");
            }

            @Override
            public void onNext(String s) {
                Log("asyncSubject: " + s);
            }
        });
    }
    /**
     * Subject之　BehaviorSubject
     * Observer会接收到BehaviorSubject被订阅之前的最后一个数据，再接收其他发射过来的数据，
     * 如果BehaviorSubject被订阅之前没有发送任何数据，则会发送一个默认数据
     *
     * 以下代码，Observer会接收到behaviorSubject2、behaviorSubject3、behaviorSubject4，
     * 如果在behaviorSubject.subscribe()之前不发送behaviorSubject1、behaviorSubject2，
     * 则Observer会先接收到default,再接收behaviorSubject3、behaviorSubject4。
     */
    private void behaviorSubject() {
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("default");
//        behaviorSubject.onNext("behaviorSubject1");
//        behaviorSubject.onNext("behaviorSubject2");
        behaviorSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log("behaviorSubject completed");
            }

            @Override
            public void onError(Throwable e) {
                Log("behaviorSubject onError");
            }

            @Override
            public void onNext(String s) {
                Log("behaviorSubject: " + s);
            }
        });
        behaviorSubject.onNext("behaviorSubject3");
        behaviorSubject.onNext("behaviorSubject4");
    }
    /**
     * Subject之　PublishSubject
     * 相对比其他Subject常用，它的Observer只会接收到PublishSubject被订阅之后发送的数据
     */
    private void publishSubject() {
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("publishSubject1");
        publishSubject.onNext("publishSubject2");
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log("publishSubject completed");
            }

            @Override
            public void onError(Throwable e) {
                Log("publishSubject onError");
            }

            @Override
            public void onNext(String s) {
                Log("publishSubject: " + s);
            }
        });
        publishSubject.onNext("publishSubject3");
        publishSubject.onNext("publishSubject4");
    }
    /**
     * Subject之　ReplaySubject
     * ReplaySubject会发射所有数据给观察者，无论它们是何时订阅的。也有其它版本的ReplaySubject，
     * 在重放缓存增长到一定大小的时候或过了一段时间后会丢弃旧的数据
     */
    private void replaySubject() {
        ReplaySubject<String> replaySubject = ReplaySubject.create();
        //replaySubject = ReplaySubject.create(100);//创建指定初始缓存容量大小为100的ReplaySubject
        //replaySubject = ReplaySubject.createWithSize(2);//只缓存订阅前最后发送的2条数据
        //replaySubject=ReplaySubject.createWithTime(1,TimeUnit.SECONDS,Schedulers.computation());  //replaySubject被订阅前的前1秒内发送的数据才能被接收
        replaySubject.onNext("replaySubject:pre1");
        replaySubject.onNext("replaySubject:pre2");
        replaySubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log("replaySubject: " + s);
            }
        });
        replaySubject.onNext("replaySubject:after1");
        replaySubject.onNext("replaySubject:after2");
    }

    private void initObserver() {
        stringReceiver = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log("stringReceiver completed");
            }

            @Override
            public void onError(Throwable e) {
                Log("stringReceiver error");
            }

            @Override
            public void onNext(String s) {
                Log("stringReceiver: " + s);
            }
        };

        longReceiver = new Observer<Long>() {
            @Override
            public void onCompleted() {
                Log("longReceiver completed");
            }

            @Override
            public void onError(Throwable e) {
                Log("longReceiver error");
            }

            @Override
            public void onNext(Long s) {
                Log("longReceiver: " + s);
            }
        };

        //如果你不在意数据是否接收完或者是否出现错误，即不需要Observer的onCompleted()和onError()方法，可使用Action1代替Observer
        intReceiver = new Action1<Integer>() {

            @Override
            public void call(Integer integer) {
                Log("intReceiver: " + integer);
            }
        };

    }

    private void Log(String content) {
        Log.d(TAG, content);
    }
}
