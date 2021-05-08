package com.studentsystem.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {

    private static OkHttpClient sOkHttpClient = new OkHttpClient();

    public static void get(String url, Callback callback) {
        final Request request = new Request.Builder()
            .url(url)
            .build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void post(String url, Callback callback) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(mediaType, requestBody))
            .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
