package com.example.jetpackdemo.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<MovieDataSource> liveDataSource = new MutableLiveData<>();

    /**
     * 只调一次
     */
    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        Log.d("yqtest", "MovieDataSourceFactory create");
        MovieDataSource dataSource = new MovieDataSource();
        liveDataSource.postValue(dataSource);
        return dataSource;
    }
}
