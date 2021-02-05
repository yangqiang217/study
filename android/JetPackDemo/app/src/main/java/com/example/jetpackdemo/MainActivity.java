package com.example.jetpackdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.jetpackdemo.databinding.ActivityMainBinding;
import com.example.jetpackdemo.paging.MoviePagingAdapter;
import com.example.jetpackdemo.bean.Movie;
import com.example.jetpackdemo.paging.MovieViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private MainViewModel mMainViewModel;
    private MovieViewModel mMovieViewModel;

    private MoviePagingAdapter mMoviePagingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        super.onCreate(savedInstanceState);
        setContentView(mActivityMainBinding.getRoot());


        initViewModel();
        initClick();
        initFragment();
        initList();

        CustomViewGroup2 customViewGroup2 = new CustomViewGroup2(
            this,
            mActivityMainBinding.custom2Container
        );
    }

    private void initViewModel() {
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
//        Log.d("yqtest", "onCreate: " + mMainViewModel.hashCode());

//        mMainViewModel.setOnTimeChangeListener(new TestViewModel.OnTimeChangeListener() {
//            @Override
//            public void onTimeChanged(int second) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv.setText(second + "");
//
//                    }
//                });
//            }
//        });

        mMainViewModel.getLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer second) {
                mActivityMainBinding.btnTest.setText(second + "");
            }
        });
    }

    private void initClick() {
        mActivityMainBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainViewModel.start();
                //included
                mActivityMainBinding.included.tvIncludeTest.setText("include change");
            }
        });
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new TestFragment());
        fragmentTransaction.commit();
    }

    private void initList() {
        mMoviePagingAdapter = new MoviePagingAdapter();
        mActivityMainBinding.rv.setAdapter(mMoviePagingAdapter);
        mActivityMainBinding.rv.setLayoutManager(new LinearLayoutManager(this));

        mMovieViewModel.getMoviePagedList().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                Log.d("yqtest", "onChanged: " + movies);
                //当数据发生变化时，该变化通过LiveData传递过来，再通过PagedLIstAdapter.submitList()方法刷新数据。
                mMoviePagingAdapter.submitList(movies);
            }
        });
    }
}