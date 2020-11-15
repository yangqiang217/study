package com.yq.imageviewer.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangqiang on 09/02/2018.
 */

public class PageItem {
    private List<String> mImageUrls = new ArrayList<>();
    /**
     * 加密过的
     */
    private String mDirName;

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    public PageItem addImageUrl(String imageUrl) {
        mImageUrls.add(imageUrl);
        return this;
    }

    public String getDirName() {
        return mDirName;
    }

    public PageItem setDirName(String dirName) {
        mDirName = dirName;
        return this;
    }
}
