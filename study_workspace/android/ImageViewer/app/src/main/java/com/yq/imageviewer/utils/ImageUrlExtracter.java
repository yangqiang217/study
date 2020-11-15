package com.yq.imageviewer.utils;

import android.text.TextUtils;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.bean.PageItem;
import com.yq.imageviewer.utils.des.DesUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yangqiang on 09/02/2018.
 * 多个页面的url抽出每张图片的url
 */
public class ImageUrlExtracter {

    public static void extractImageUrl(final String url, final String titleAndTime, final OnExtractListener onExtractListener) {

        final List<PageItem> imageUrls = new ArrayList<>();
        Observable.just(url)
            .map(new Func1<String, Document>() {
                @Override
                public Document call(String pageUrl) {
                    publishProgress("extracting " + pageUrl, onExtractListener);
                    try {
                        return Jsoup.connect(pageUrl).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            })
            .map(new Func1<Document, PageItem>() {
                @Override
                public PageItem call(Document document) {
                    PageItem pageItem = new PageItem();

                    publishProgress("title_time: " + titleAndTime, onExtractListener);
                    pageItem.setDirName(DesUtil.encrypt(titleAndTime));

                    for (Element element : document.select("img")) {
                        String datatype = element.attr("data-type");
                        if (!"jpeg".equals(datatype)) {
                            continue;
                        }
                        String url = element.attr("data-src");
                        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
                            pageItem.addImageUrl(url.trim());
                        }
                    }
                    return pageItem;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<PageItem>() {
                @Override
                public void onCompleted() {
                    if (onExtractListener != null) {
                        publishProgress("extract all finish", onExtractListener);
                        onExtractListener.onSuccess(imageUrls);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (onExtractListener != null) {
                        onExtractListener.onError(e);
                    }
                }

                @Override
                public void onNext(PageItem strings) {
                    imageUrls.add(strings);
                }
            });
    }

    private static void publishProgress(String msg, OnExtractListener listener) {
        if (listener != null) {
            listener.onProgress(msg);
        }
    }

    public interface OnExtractListener {
        void onError(Throwable throwable);
        void onProgress(String msg);
        void onSuccess(List<PageItem> imageUrls);
    }
}
