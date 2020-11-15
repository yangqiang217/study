package com.yq.imageviewer.bean;

/**
 * Created by yangqiang on 09/02/2018.
 */

public class ImageItem {
    private String mUrl;
    private String mParentDir;
    private String mFileName;

    public String getUrl() {
        return mUrl;
    }

    public ImageItem setUrl(String url) {
        mUrl = url;
        return this;
    }

    public String getFileName() {
        return mFileName;
    }

    public ImageItem setFileName(String fileName) {
        mFileName = fileName;
        return this;
    }

    public String getParentDir() {
        return mParentDir;
    }

    public ImageItem setParentDir(String parentDir) {
        mParentDir = parentDir;
        return this;
    }
}
