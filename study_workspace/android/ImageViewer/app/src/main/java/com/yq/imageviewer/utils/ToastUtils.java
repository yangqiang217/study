package com.yq.imageviewer.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.yq.imageviewer.MyApplication;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wbp on 2017/6/27.
 */

public class ToastUtils {
    private static Toast toast;

    private ToastUtils() {
    }

    public static void show(final String content) {
        if (TextUtils.isEmpty(content)) return;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(content);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        initToast();
                        toast.setText(content);
                        toast.show();
                    }
                });
    }

    private static void initToast() {
        if (toast == null) {
            synchronized (ToastUtils.class) {
                if (toast == null) {
                    toast = Toast.makeText(MyApplication.getAppContext(), "", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public static void show(int strRes) {
        String content = MyApplication.getAppContext().getResources().getString(strRes);
        show(content);
    }

    public static void hide() {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        initToast();
                        toast.cancel();
                    }
                });
    }
}
