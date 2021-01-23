package com.example.jetpackdemo.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.jetpackdemo.bean.Movie;
import com.example.jetpackdemo.paging.itemkeyed.ItemKeyDataSource;
import com.example.jetpackdemo.paging.pagekeyed.PagedDataSource;
import com.example.jetpackdemo.paging.positional.PosDataSource;

/**
 * 负责创建DataSource,
 * 并使用LiveData包装DataSource,将其暴露给ViewModel
 * 三种是通用的
 * 泛型第一个参数的作用在ItemKeyDataSource里能提现出来
 */
public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<PosDataSource> posLiveDataSource = new MutableLiveData<>();
    private MutableLiveData<PagedDataSource> pagekeyedLiveDataSource = new MutableLiveData<>();
    private MutableLiveData<ItemKeyDataSource> itemkeyLiveDataSource = new MutableLiveData<>();

    /**
     * 只调一次
     */
    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        Log.d("yqtest", "MovieDataSourceFactory create");

        return createPagedDataSource();
//        return createPositionalKeyDataSource();
//        return createItemKeyDataSource();
    }

    private DataSource<Integer, Movie> createItemKeyDataSource() {
        ItemKeyDataSource dataSource = new ItemKeyDataSource();
        itemkeyLiveDataSource.postValue(dataSource);

        return dataSource;
    }

    private DataSource<Integer, Movie> createPagedDataSource() {
        PagedDataSource dataSource = new PagedDataSource();
        pagekeyedLiveDataSource.postValue(dataSource);

        return dataSource;
    }

    private DataSource<Integer, Movie> createPositionalKeyDataSource() {
        PosDataSource dataSource = new PosDataSource();
        posLiveDataSource.postValue(dataSource);

        return dataSource;
    }
}
