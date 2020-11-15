
package com.yq.tjimagereadandshow;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BitmapManager {
    /**
     * 序号不能轻易改，验证流程、预览以及修改流程都很严格的在使用
     */
    public static final int MAX_BITMAP_NUM = 200;

    private static BitmapManager mBitmapManager;
    private HashMap<String, WeakReference<Bitmap>> mPreviewBitmapMap;
    private HashMap<String, WeakReference<Bitmap>> mBitmapMap;

    private BitmapManager () {
        mBitmapMap = new HashMap<String, WeakReference<Bitmap>>();
        mPreviewBitmapMap = new HashMap<String, WeakReference<Bitmap>>();
    }

    public static BitmapManager getInstance() {
        if (mBitmapManager == null) {
            mBitmapManager = new BitmapManager();
        }

        return mBitmapManager;
    }

    public Bitmap getPreviewBitmap(String filePath) {
        WeakReference<Bitmap> softBmp = null;
        Bitmap bmp = null;
        softBmp = mPreviewBitmapMap.get(filePath);
        if (softBmp != null) {
            bmp = softBmp.get();
            if (bmp != null) {
                return bmp;
            }
        }
        releasePreviewBitmap();
        
        int width, height;
        if (MainActivity.widthPixels > MainActivity.heightPixels) {
            height = MainActivity.widthPixels;
            width = MainActivity.heightPixels;
        } else {
            width = MainActivity.widthPixels;
            height = MainActivity.heightPixels;
        }

        Bitmap normalBitmap = null;
        try {
            normalBitmap = ImageUtil.getNormalBitmap(filePath, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            System.gc();
            return null;
        } catch (OutOfMemoryError oe) {
            oe.printStackTrace();
            System.gc();
            return null;
        }
        if (normalBitmap == null) {
            return null;
        }
        softBmp = new WeakReference<Bitmap>(normalBitmap);
        
        mPreviewBitmapMap.put(filePath, softBmp);

        return normalBitmap;
    }

    public void releasePreviewBitmap() {
        Bitmap bmp = null;
        for (WeakReference<Bitmap> softBmp : mPreviewBitmapMap.values()) {
            bmp = softBmp.get();
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
            }
            bmp = null;
        }
        mPreviewBitmapMap.clear();
    }


    /**
     * @see: 此函数是带压缩功能的getBitmapByKey(int index, String fp)
     * @param needCompress：是否需要压缩
     * @return：有可能返回NULL
     */
    public Bitmap getBitmap(String filePath, boolean needCompress) {
        WeakReference<Bitmap> softBmp = null;
        Bitmap bmp = null;
        if (!isFileExit(filePath)) {
            return null;
        }
        if (mBitmapMap == null) {
            return null;
        } else {
            softBmp = mBitmapMap.get(filePath);
            if (softBmp != null) {
                bmp = softBmp.get();
            }
            if (softBmp == null || bmp == null) {
                if (!addBitmap(filePath, needCompress)) {
                    return null;
                }
                softBmp = mBitmapMap.get(filePath);
                if (softBmp != null) {
                    bmp = softBmp.get();
                }
            }
        }
        return bmp;
    }

    /**
     * @see: 判断文件是否存在
     * @return
     */
    private boolean isFileExit(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.length() > 0;
    }

    /**
     * @see：如果需要压缩，需要事先设置好activity，否则返回false
     * @param needCompress：是否需要压缩
     * @return 是否添加成功
     */
    public boolean addBitmap(String filePath, boolean needCompress) {
        if (mBitmapMap.size() > MAX_BITMAP_NUM) {
            List<String> empty = new ArrayList<String>();
            for (String key : mBitmapMap.keySet()) {
                WeakReference<Bitmap> softBmp = mBitmapMap.get(filePath);
                if (softBmp == null || softBmp.get() == null) {
                    empty.add(key);
                }
            }
            for (String key : empty) {
                mBitmapMap.remove(key);
            }
            empty.clear();
        }

        Bitmap normalBitmap;
        if (needCompress) {
            try {
                PhotoUtil.compresImageByBitmap(null, filePath, false);
            } catch (Exception e) {
                e.printStackTrace();
                System.gc();
                return false;
            } catch (OutOfMemoryError oe) {
                oe.printStackTrace();
                System.gc();
                return false;
            }
        }
        // 获取小的缩略图用户显示
        normalBitmap = ImageUtil.getSmallBitmap(filePath);

        if (normalBitmap == null) {
            return false;
        }

        mBitmapMap.put(filePath, new WeakReference<Bitmap>(normalBitmap));

        return true;
    }
}
