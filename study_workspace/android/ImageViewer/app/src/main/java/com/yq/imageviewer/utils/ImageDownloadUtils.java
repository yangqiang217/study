package com.yq.imageviewer.utils;

import com.yq.imageviewer.bean.ImageItem;
import com.yq.imageviewer.bean.PageItem;
import com.yq.imageviewer.network.DownloadProgressCallback;
import com.yq.imageviewer.network.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ImageDownloadUtils {

    public static final int DOWNLOAD_FIRST_IMAGE_NAME = 1;

    public static void download(List<PageItem> pageItems, final DownloadProgressCallback callBack) {
        publishProgress("start download image", callBack);

        Observable.from(pageItems)
            .filter(new Func1<PageItem, Boolean>() {
                @Override
                public Boolean call(PageItem pageItem) {
                    publishProgress("downloading page " + pageItem.getDirName(), callBack);
                    return FileUtil.checkDirectory(pageItem.getDirName());
                }
            })
            .map(new Func1<PageItem, List<ImageItem>>() {
                @Override
                public List<ImageItem> call(PageItem pageItem) {
                    List<ImageItem> imageItems = new ArrayList<>();
                    for (int i = 0; i < pageItem.getImageUrls().size(); i++) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.setFileName(i + DOWNLOAD_FIRST_IMAGE_NAME + "");
                        imageItem.setUrl(pageItem.getImageUrls().get(i));
                        imageItem.setParentDir(pageItem.getDirName());

                        imageItems.add(imageItem);
                    }
                    return imageItems;
                }
            })
            .flatMap(new Func1<List<ImageItem>, Observable<ImageItem>>() {
                @Override
                public Observable<ImageItem> call(List<ImageItem> imageItems) {
                    return Observable.from(imageItems);
                }
            })
            .doOnNext(new Action1<ImageItem>() {
                @Override
                public void call(ImageItem imageItem) {
                    try {
                        publishProgress("downloading image " + imageItem.getFileName() + ", " + imageItem.getUrl(), callBack);
                        ResponseBody body = ServiceFactory.makeDownloadService().downloadFile(imageItem.getUrl()).execute().body();
                        FileUtil.saveResponseToFile(body, imageItem.getParentDir(), imageItem.getFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(imageItem.getUrl());
                    }
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<ImageItem>() {
                @Override
                public void onCompleted() {
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (callBack != null) {
                        callBack.onError(e.getMessage());
                    }
                    e.printStackTrace();
                }

                @Override
                public void onNext(ImageItem imageItem) {

                }
            });
    }

    private static void publishProgress(String msg, DownloadProgressCallback listener) {
        if (listener != null) {
            listener.onProgress(msg);
        }
    }
}
