package com.example.yangqiang.retrofit2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yangqiang on 02/01/2018.
 */

public interface ReqInterface {

    @GET("/api/columns/{user}")
    Call<ZhuanLanAuthor> getAuthor(@Path("user") String user);

    @GET("/api/columns/{user}")
    Observable<ZhuanLanAuthor> getAuthorRx(@Path("user") String user);
}
