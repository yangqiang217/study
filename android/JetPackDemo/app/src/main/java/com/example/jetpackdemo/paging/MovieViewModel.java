package com.example.jetpackdemo.paging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.jetpackdemo.bean.Movie;
import com.example.jetpackdemo.paging.positional.PosDataSource;

import java.util.Timer;
import java.util.TimerTask;

public class MovieViewModel extends ViewModel {

    //三种通用
    private LiveData<PagedList<Movie>> moviePagedList;

    public MovieViewModel() {
        //三种通用
        PagedList.Config config = (new PagedList.Config.Builder())
            /*
            是否需要为那些"数量已知，但尚未加载处理的数据"预留位置。 例如，我们通过loadInitial()方法首次请求数据，
            获取了8部电影的数据，并获知一共有70部新电信在上映，如果设置setEnablePlaceholders()为true，并且通过
            callback.onResult()方法的totalCount将70告诉了PagedList.那么RecyclerView一共会为你预留70个Item
            的位置。此时，将网络关闭。也可以看到，另外68个Item的效果。
            需要注意的是:setEnablePlaceholders()默认为true，如果数据量很大的话，请设置为false，不然会消耗不必要的性能
             */
            .setEnablePlaceholders(false)
            .setPageSize(PosDataSource.PER_PAGE)
            //设置当距离底部还有多少条数据时开始加载下一页数据
            .setPrefetchDistance(1)
            //设置首次加载数据的数量。该值要求是PageSize的整数倍。若未设置，则默认是PageSize的3倍。
            .setInitialLoadSizeHint(PosDataSource.PER_PAGE)
            .setMaxSize(65536 * PosDataSource.PER_PAGE)
            .build();

        moviePagedList = (new LivePagedListBuilder<>(new MovieDataSourceFactory(), config)).build();
    }

    public LiveData<PagedList<Movie>> getMoviePagedList() {
        return moviePagedList;
    }
}
