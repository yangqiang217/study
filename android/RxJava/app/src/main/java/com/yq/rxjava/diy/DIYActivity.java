package com.yq.rxjava.diy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yq.rxjava.L;
import com.yq.rxjava.MainActivity;
import com.yq.rxjava.R;

import com.yq.rxjava.diy.sched.AndroidSchedulers;
import com.yq.rxjava.diy.sched.Schedulers;

import java.util.concurrent.TimeUnit;

import rx.functions.Func1;

/**
 * http://blog.csdn.net/tellh/article/details/71534704
 */
public class DIYActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_i_y);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DIYActivity.this, MainActivity.class));
            }
        });
    }

    private void test() {
        //返回ObservableC，里面OnSubscribeC，call就是自定义的call
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriberM) {
                L.print("call in create");
                subscriberM.onNext(111);
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        })
            //调用ObservableC的map，返回ObservableM，里面OnSubscribeM
            .map(new Func1<Integer, Integer>() {
                @Override
                public Integer call(Integer integer) {
                    L.print("map1");
                    return integer + 1;
                }
            })

            //调用ObservableM的subscribeOn，返回ObservableS，里面OnSubscribeS
            .subscribeOn(Schedulers.io())

            //调用ObservableS的mapMid，返回ObservableMMid，里面OnSubscribeMMid
            .mapMid(new Func1<Integer, Integer>() {
                @Override
                public Integer call(Integer integer) {
                    L.print("map mid");
                    return integer;
                }
            })

            //调用ObservableMMid的observeOn，返回ObservableB，里面OnSubscribeB
            .observeOn(AndroidSchedulers.mainThread())

            //调用ObservableB的map2，返回ObservableM2，里面OnSubscribeM2
            .map2(new Func1<Integer, Integer>() {
                @Override
                public Integer call(Integer integer) {
                    L.print("map2");
                    return integer - 1;
                }
            })

            //调用ObservableM2的subscribe()，里面onSubscribe.call的是ObservableM2的
            .subscribe(new Subscriber<Integer>() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {
                    L.print(e.getMessage());
                }

                @Override
                public void onNext(Integer s) {
                    L.print("onNext " + s);
                }
            });

    }
}