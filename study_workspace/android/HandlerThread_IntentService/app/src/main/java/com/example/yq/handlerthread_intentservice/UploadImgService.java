package com.example.yq.handlerthread_intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;


/**
 */
public class UploadImgService extends IntentService {

    private static final String ACTION_UPLOAD_IMG = "com.example.yq.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.example.yq..intentservice.extra.IMG_PATH";

    public static void startUploadImg(Context context, String path) {
        Intent intent = new Intent(context, UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }

    public UploadImgService() {
        super("UploadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println(Thread.currentThread().getName());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMG.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUploadImg(path);
            }
        }
    }

    private void handleUploadImg(String path) {
        try {
            TimeUnit.SECONDS.sleep(2);

            Intent intent = new Intent(IntentServcieActivity.UPLOAD_RESULT);
            intent.putExtra(EXTRA_IMG_PATH, path);
            sendBroadcast(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
