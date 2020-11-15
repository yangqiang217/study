package com.yq.imageviewer.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceFactory {

    /**
     * 视频下载service
     * 和别处写法不同，因为这里需要传参数
     */
    private static class VideoDownloadServiceHolder {
        //这里不能用单例，因为callback每个视频都不一样的，单例的话callback就是第一个视频的callback都不变了
        private synchronized static FileTransferApi makeRxVideoDownloadAPI() {
            return makeRetrofit().create(FileTransferApi.class);
        }

        private static Retrofit makeRetrofit() {
            return new Retrofit.Builder()
                //baseurl是必须要有，不然直接崩，但是如果请求中用@Url指定了动态url，那么baseurl不会起作用，
                // 见http://www.jianshu.com/p/4268e434150a
                .baseUrl("http://www.baidu.com")
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(HttpClientManager.getClient())
                .build();
        }
    }

    /**
     * 文件下载请求调用这个
     */
    public static FileTransferApi makeDownloadService() {
        return VideoDownloadServiceHolder.makeRxVideoDownloadAPI();
    }
}
