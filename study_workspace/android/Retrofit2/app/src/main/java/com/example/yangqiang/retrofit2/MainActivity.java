package com.example.yangqiang.retrofit2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://zhuanlan.zhihu.com";

    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                normalRetrofitReq();
                rxRetrofitReq();
            }
        });


    }

    /**
     * retrofit原生形式的请求
     */
    private void normalRetrofitReq() {
        ReqInterface api = makeApi(false);
        Call<ZhuanLanAuthor> call = api.getAuthor("qinchao");

        call.enqueue(new Callback<ZhuanLanAuthor>() {
            @Override
            public void onResponse(Call<ZhuanLanAuthor> call, Response<ZhuanLanAuthor> response) {
                System.out.println("succ");
            }

            @Override
            public void onFailure(Call<ZhuanLanAuthor> call, Throwable t) {
                System.out.println("fail");
            }
        });
    }

    /**
     * rxjava形式的请求
     */
    private void rxRetrofitReq() {
        makeApi(true).getAuthorRx("qinchao")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<ZhuanLanAuthor>() {
                @Override
                public void onCompleted() {
                    System.out.println("onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("onError");
                    e.printStackTrace();
                }

                @Override
                public void onNext(ZhuanLanAuthor zhuanLanAuthor) {
                    System.out.println("onNext");
                }
            });
    }

    private ReqInterface makeApi(boolean rx) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create());
        if (rx) {
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }
        //返回代理ReqInterface
        return builder.build().create(ReqInterface.class);
    }
}
