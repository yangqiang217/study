package com.yq.imageviewer.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.utils.des.DesUtil;

import java.io.File;
import java.io.Serializable;

/**
 * Created by yangqiang on 08/02/2018.
 */
public class CoverItem implements Serializable, Comparable {

    private File mDirectory;
    private String mTitle;
    private String mPublishDate;
    private File mCoverFile;
    private int mImgOriginalWidth;
    private int mImgOriginalHeight;
    private float mRatio;

    public File getDirectory() {
        return mDirectory;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    private void setPublishDate(String publishDate) {
        mPublishDate = publishDate;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public void setDirectory(File directory) {
        mDirectory = directory;
        setTitleAndPubDate();
    }

    public File getCoverFile() {
        return mCoverFile;
    }

    public void setCoverFile(File coverFile) {
        mCoverFile = coverFile;
    }

    public int getImgOriginalWidth() {
        return mImgOriginalWidth;
    }

    public void setImgOriginalWidth(int imgOriginalWidth) {
        mImgOriginalWidth = imgOriginalWidth;
    }

    public int getImgOriginalHeight() {
        return mImgOriginalHeight;
    }

    public void setImgOriginalHeight(int imgOriginalHeight) {
        mImgOriginalHeight = imgOriginalHeight;
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    private void setTitleAndPubDate() {
        String titleAndDate = null;
        try {
            titleAndDate = DesUtil.decrypt(mDirectory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(titleAndDate)) {
            if (titleAndDate.contains(Const.GAP_TITLE_TIME)) {
                String[] s = titleAndDate.split(Const.GAP_TITLE_TIME);
                mTitle = s[0];
                mPublishDate = s[1];
                if (mPublishDate.contains("肉丝")) {
                    System.out.println();
                }
            } else {
                mTitle = titleAndDate;
                mPublishDate = "";
            }
        }
    }

    public static CoverItem getDefault(File directory) {
        CoverItem item = new CoverItem();
        item.setTitle("null");
        item.setCoverFile(new File(""));
        item.setDirectory(directory);
        item.setPublishDate("");
        item.setRatio(1);
        return item;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        String osDate = ((CoverItem) o).mPublishDate;
        if (TextUtils.isEmpty(osDate) && TextUtils.isEmpty(mPublishDate)) {
            return 0;
        } else if (TextUtils.isEmpty(osDate) && !TextUtils.isEmpty(mPublishDate)) {
            return -1;
        } else if (!TextUtils.isEmpty(osDate) && TextUtils.isEmpty(mPublishDate)) {
            return 1;
        }

        String[] myStrs = mPublishDate.split("-");
        String[] oStrs = osDate.split("-");

        if (!checkNotNull(myStrs) || !checkNotNull(oStrs)) {
            return 0;
        }

        for (int i = 0; i < myStrs.length; i++) {
            if (!myStrs[i].equals(oStrs[i])) {
                int a = Integer.parseInt(oStrs[i]);
                int b = Integer.parseInt(myStrs[i]);
                return a - b;
            }
        }
        return 0;
    }

    private boolean checkNotNull(String[] strings) {
        if (strings == null || strings.length != 3) {
            return false;
        }
        for (String s : strings) {
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }
}
