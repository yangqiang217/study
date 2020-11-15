package com.yq.tjimagegallery;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by v-yangqiang on 2016/5/28.
 */
public class ImageItemDataInfo {

    public String mPicTrueId;
    public String mPictruePath; // 图片路径
    public int mNotEdit;
    public int mRotate; //  图片旋转角度
    public int mTagCount;
    public ArrayList<PicTagInfo> mTagArray = new ArrayList<PicTagInfo>(); // 名字集合

    public static class PicTagInfo implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 981052818530463743L;

        private float mLocX; // tag在图片的位置，横向
        private float mLocY; // tag在图片的位置，纵向
        private String mName; // 图片名称

        public PicTagInfo(float mLocX, float mLocY, String mName) {
            this.mLocX = mLocX;
            this.mLocY = mLocY;
            this.mName = mName;
        }

        public float getmLocX() {
            return mLocX;
        }

        public float getmLocY() {
            return mLocY;
        }

        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }
    }
}
