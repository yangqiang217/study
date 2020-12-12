package com.yq.imageviewer.network;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;

/**
 * @author sparkchen on 16/12/17.
 */

public class HttpClientManager {

    private static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableList(
        Protocol.HTTP_2, Protocol.HTTP_1_1);

    /**
     * 下载
     */
    private static class DownloadClientHolder {
        private final static OkHttpClient CLIENT = generateClient();

        private static OkHttpClient generateClient() {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS)
                .readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS)
                .protocols(DEFAULT_PROTOCOLS)
                .writeTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);

            return clientBuilder.build();
        }
    }

    public static OkHttpClient getClient() {
        return DownloadClientHolder.CLIENT;
    }
}
