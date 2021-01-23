package com.example.jetpackdemo.paging.pagekeyed;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.jetpackdemo.bean.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 适用于数据源以"页"的方式进行请求的情况。例如，若请求时携带的参数为page=2&pageSize=5,则表示数据源以5条数据为一页，当前返回第二页的5条数据。
 */
public class PagedDataSource extends PageKeyedDataSource<Integer, Movie> {

    public static final int FIRST_PAGE = 0;
    public static final int PER_PAGE = 10;
    private int start = 0;

    //第一次请求会调用这个方法，在子线程
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        Log.d("yqtest", "loadInitial: " + "loadAfter, params.placeholdersEnabled: " + params.placeholdersEnabled
            + ", params.requestedLoadSize: " + params.requestedLoadSize
            + ", " + Thread.currentThread().getName());

        //在此处进行网络请求
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Movie> list = new ArrayList<>();
        for (int i = start; i < PER_PAGE; i++) {
            list.add(new Movie(i, String.valueOf(i), i + 0.1f));
        }
        callback.onResult(list, null, FIRST_PAGE + 1);
        start += PER_PAGE;
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        Log.d("yqtest", "loadBefore, params.key: " + params.key
            + ", params.requestedLoadSize: " + params.requestedLoadSize
            + ", " + Thread.currentThread().getName());
    }

    /**
     *
     * @param params 在loadInitial()方法中设置的nextPageKey,正是通过LoadParams传递过来的。
     *               LoadParams.key得到的是下一页的key，通过这个key，我们就可以请求下一页
     * @param callback
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        Log.d("yqtest", "loadAfter, params.key: " + params.key
            + ", params.requestedLoadSize: " + params.requestedLoadSize
            + ", " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<Movie> list = new ArrayList<>();
        for (int i = start; i < start + PER_PAGE; i++) {
            list.add(new Movie(i, String.valueOf(i), i + 0.1f));
        }
        start += PER_PAGE;

        boolean hasMore = false;
        Integer nextKey = hasMore ? params.key + 1 : null;
        callback.onResult(list, nextKey);
    }
}
