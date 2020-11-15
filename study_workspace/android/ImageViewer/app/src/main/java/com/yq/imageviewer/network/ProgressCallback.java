package com.yq.imageviewer.network;

/**
 * 下载的进度接口
 */
public interface ProgressCallback {
    void onProgress(String msg);

    void onError(String msg);
}
