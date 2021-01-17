package com.example.jetpackdemo.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovieDataSource extends PositionalDataSource<Movie> {

    public static final int PER_PAGE = 10;
    private int start = 0;

    //第一次请求会调用这个方法，在子线程
    @Override
    public void loadInitial(@NonNull LoadInitialParams params,
        @NonNull LoadInitialCallback<Movie> callback) {
        Log.d("yqtest", "loadInitial: " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Movie> list = new ArrayList<>();
        for (int i = start; i < PER_PAGE; i++) {
            list.add(new Movie(String.valueOf(i), i + 0.1f));
        }
        callback.onResult(list, start, 100);
        start += PER_PAGE;
    }

    //接下来的每次请求都会调用该方法
    @Override
    public void loadRange(@NonNull LoadRangeParams params,
        @NonNull LoadRangeCallback<Movie> callback) {
        Log.d("yqtest", "loadRange: " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<Movie> list = new ArrayList<>();
        for (int i = start; i < start + PER_PAGE; i++) {
            list.add(new Movie(String.valueOf(i), i + 0.1f));
        }
        start += PER_PAGE;
        callback.onResult(list);
    }
}
