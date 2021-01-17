package com.example.jetpackdemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.jetpackdemo.list.Movie;
import com.example.jetpackdemo.list.MovieDataSource;
import com.example.jetpackdemo.list.MovieDataSourceFactory;

import java.util.Timer;
import java.util.TimerTask;

public class MainViewModel extends ViewModel {


    private Timer timer;
    private int curr;
    //    private OnTimeChangeListener onTimeChangeListener;

    //用liveData替换listener
    private MutableLiveData<Integer> liveData = new MutableLiveData<>();

    private LiveData<PagedList<Movie>> moviePagedList;

    public MainViewModel() {
        PagedList.Config config = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPageSize(MovieDataSource.PER_PAGE)
            .setPrefetchDistance(1)
            .setInitialLoadSizeHint(MovieDataSource.PER_PAGE * 4)
            .setMaxSize(65536 * MovieDataSource.PER_PAGE)
            .build();

        moviePagedList = (new LivePagedListBuilder<>(new MovieDataSourceFactory(), config)).build();
    }

    public MutableLiveData<Integer> getLiveData() {
        return liveData;
    }

    public LiveData<PagedList<Movie>> getMoviePagedList() {
        return moviePagedList;
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    curr++;
                    if (liveData != null) {
                        liveData.postValue(curr);
                    }
//                    if (onTimeChangeListener != null)
//                        onTimeChangeListener.onTimeChanged(curr);
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    //    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
//        this.onTimeChangeListener = onTimeChangeListener;
//    }

//    public interface OnTimeChangeListener {
//        void onTimeChanged(int second);
//    }
}
