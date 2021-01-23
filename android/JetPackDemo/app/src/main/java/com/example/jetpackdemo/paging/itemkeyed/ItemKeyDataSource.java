package com.example.jetpackdemo.paging.itemkeyed;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.example.jetpackdemo.bean.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 适用于当目标数据的下一页需要依赖于上一页数据中最后一个对象中的某个字段作为key的情况。此类分页形式常见于评论功能的实现。
 * 例如，若上一页数据中最后一个对象的key为9527,那么在请求下一页时，需要携带参数since=9527&pageSize=5,则服务器会返回key=9527之后的5条数据
 */
public class ItemKeyDataSource extends ItemKeyedDataSource<Integer, Movie> {

    public static final int PER_PAGE = 10;
    private int start = 0;

    //第一次请求会调用这个方法，在子线程
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Movie> callback) {
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
        callback.onResult(list);
        start += PER_PAGE;
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Movie> callback) {
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
        callback.onResult(list);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Movie> callback) {

    }

    /**
     * item是当前页面最后一个item
     */
    @NonNull
    @Override
    public Integer getKey(@NonNull Movie item) {
        Log.d("yqtest", "getKey, item.id: " + item.getId());
        return item.getId();
    }
}
