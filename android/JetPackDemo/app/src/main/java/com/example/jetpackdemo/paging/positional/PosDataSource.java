package com.example.jetpackdemo.paging.positional;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.example.jetpackdemo.bean.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/qq_43404873/article/details/109668063
 * 适用于可通过任意位置加载数据，且目标数据源数量固定的情况。例如，若请求时携带的参数为start=2&count=5,则表示向服务端请求从第2条数据开始往后的5条数据。
 * 就限制好了用int
 */
public class PosDataSource extends PositionalDataSource<Movie> {

    public static final int PER_PAGE = 10;

    //第一次请求会调用这个方法，在子线程
    @Override
    public void loadInitial(@NonNull LoadInitialParams params,
        @NonNull LoadInitialCallback<Movie> callback) {

        Log.d("yqtest", "loadInitial: " + Thread.currentThread().getName());

        //在此处进行网络请求
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Movie> list = new ArrayList<>();
        for (int i = 0; i < PER_PAGE; i++) {
            list.add(new Movie(i, String.valueOf(i), i + 0.1f));
        }
        callback.onResult(list, 0, 100);
    }

    //接下来的每次请求都会调用该方法
    @Override
    public void loadRange(@NonNull LoadRangeParams params,
        @NonNull LoadRangeCallback<Movie> callback) {

        Log.d("yqtest", "loadRange, startPos: " + params.startPosition//10
            +  ", " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<Movie> list = new ArrayList<>();
        for (int i = params.startPosition; i < params.startPosition + PER_PAGE; i++) {
            list.add(new Movie(i, String.valueOf(i), i + 0.1f));
        }
        callback.onResult(list);
    }
}
