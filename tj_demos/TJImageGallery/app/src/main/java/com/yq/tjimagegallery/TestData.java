package com.yq.tjimagegallery;

import java.util.ArrayList;

/**
 * Created by v-yangqiang on 2016/5/28.
 */
public enum TestData {
    INTANCE;

    private ArrayList<ImageItemDataInfo> list = new ArrayList<ImageItemDataInfo>();;

    public ArrayList<ImageItemDataInfo> makePicData () {
        if (list.size() == 0) {
            for (int i = 0; i < 300; i++) {
                ImageItemDataInfo info = new ImageItemDataInfo();
                info.mPicTrueId = String.valueOf(i);
                info.mPictruePath = "/storage/emulated/0/Pictures/Screenshots/123456.png";

                list.add(info);
            }
        }
        return list;
    }
}
