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
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {//subscriber是observeOn里面call里new出来的 todo
                L.print("call in create");
                subscriber.onNext(111);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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