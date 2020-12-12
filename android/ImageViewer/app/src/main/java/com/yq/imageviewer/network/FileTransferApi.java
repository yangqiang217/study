package com.yq.imageviewer.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by qiangyang on 2017/5/23.
 * 视频上传、下载的retrofit接口，目前rpc文件里没自动生成的，后期有的话再挪
 */
public interface FileTransferApi {

    /**
     * Streaming的作用:可参考https://www.jianshu.com/p/92bb85fc07e8
     *
     * 其实在源码里，请求有返回后（不包括大文件解析），会走OkhttpCall的parseResponse，parseResponse中通过serviceMethod.toResponse(catchingBody)解析结果，
     * toResponse()里调用responseConverter.convertAllToEncrypt(body)，然后这里的responseConverter是BuiltInConverters的responseBodyConverter()方法返回，这个方法
     * 会看有没有Streaming注解来返回不同的converter，有的话返回StreamingResponseBodyConverter，它的convert方法照原样返回，而没有的话返回BufferingResponseBodyConverter
     * 它的convert方法会走body.source().readAll()来整个读取出来
     *
     * 所以Streaming作用就是：拿UpdateUtil为例，如果有的话，ServiceFactory.makeDownloadService(callBack, targetFile).downloadFile(url)走完
     * 会立马（也就一个普通请求的使劲）走onNext，这样就能在onNext返回的response中保存数据到本地；而没有Streaming的话，makeDownloadService.downloadFile(url)和onNext之间
     * 包括一个普通请求+读取文件流到内存这整个的时间
     *
     * 所以如果想实现非完全保存到内存的文件下载，一种方式就是目前的方法，在onNext中解析responsebody，另一种请看和这个注释一起提交的删除的类ProgressIntercepter
     *
     * 有一点要注意的是，关于ProgressIntercepter的原理，假定现在都没有Streaming。用ProgressIntercepter的时候耗时主要集中在里面download方法，download执行完后就回调外面的onNext了，
     * 而如果没有ProgressIntercepter，也会耗费同样的时间，也就是说文件解析工作你如果不在ProgressIntercepter的download方法里执行，其它地方也会执行一次，这是为什么？
     * 其实如果有ProgressIntercepter，在执行完ProgressIntercepter后，就像上面说的，会走OkhttpCall的parseResponse，最终走到Http1Codec的read(Buffer sink, long byteCount)，
     * 里面判断了bytesRemaining，如果走了ProgressIntercepter的download，这个bytesRemaining为0，从而返回-1，导致不走while循环，所以走了ProgressIntercepter就不会走自带的默认的
     * 读流代码了
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFileRx(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
