package com.yq.okhttp3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnClick onClick = new OnClick();
        findViewById(R.id.btnAsyncGet).setOnClickListener(onClick);
        findViewById(R.id.btnAsyncPost).setOnClickListener(onClick);
    }

    //异步get
    private void asyncGet() {
        String url = "http://192.168.31.15:9999/config";
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory() + "/okhttp_cache/"), 100 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(new LoggingInterceptor())
            .build();
        
        //param
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        builder.addQueryParameter("name", "nname");
        builder.addQueryParameter("psd", "ppsd");

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .method("GET", null)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Log.d(TAG, "onResponse, current thread: " + Thread.currentThread().getName() + ", body: " + body.string());
            }
        });
    }

    //异步post
    private void asyncPost() {
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody formBody = new FormBody.Builder().add("name", "nname").add("psd", "ppsd").build();

        Request request = new Request.Builder()
                .url("http://192.168.31.15:9999/posttest")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Log.d(TAG, "onResponse, current thread: " + Thread.currentThread().getName() + ", body: " + body.string());
            }
        });
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAsyncGet:
                    asyncGet();
                    break;
                case R.id.btnAsyncPost:
                    asyncPost();
                    break;
            }
        }
    }
}
