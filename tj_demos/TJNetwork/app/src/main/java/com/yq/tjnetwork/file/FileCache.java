
package com.yq.tjnetwork.file;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileCache {
    public static final int TYPE_NORMAL_IMAGE = 0;
    public static final int TYPE_BIG_IMAGE = 1;
    public static final int TYPE_CATEGORY_IMAGE = 2;
    public static final int TYPE_JSON = 3;
    public static final String APK_TEMP_CACHE = "/kl688/cache/data/";
    public static final String LOCAL_ALBUM = "/kl688/";
    public static final String JOKE_CACHE_FILE = "joke_cache_file.json"; // 笑话缓存的内容区域

    private static final String[] DISK_CACHE_PATHS = {
            "/kl688/cache/image/normal/", "/kl688/cache/image/big/",
            "/kl688/cache/image/category/", "/kl688/cache/data/"
    };

    private static final int[] DISK_CACHE_MAXS = {
            500, 500, 100
    };

    private String[] mDiskCachePaths = null;
    private boolean[] mCacheEnable = null;
    private String mJipinAlbumPath = null;

    private static FileCache mInstance;

    public synchronized static FileCache getInstance() {
        if (mInstance == null) {
            mInstance = new FileCache();
        }
        return mInstance;
    }

    public FileCache () {
        init();
    }

    public void clear() {
        for (int i = 0; i < DISK_CACHE_PATHS.length; i++) {
            mDiskCachePaths[i] = Environment.getExternalStorageDirectory().getAbsolutePath() + DISK_CACHE_PATHS[i];
            FileUtil.deleteSubFile(mDiskCachePaths[i]);
        }
    }

    private void init() {
        mDiskCachePaths = new String[DISK_CACHE_PATHS.length];
        mCacheEnable = new boolean[DISK_CACHE_PATHS.length];
        for (int i = 0; i < DISK_CACHE_PATHS.length; i++) {
            try {
                mDiskCachePaths[i] = Environment.getExternalStorageDirectory().getAbsolutePath() + DISK_CACHE_PATHS[i];
                File file = new File(mDiskCachePaths[i]);
                file.mkdirs();
                mCacheEnable[i] = file.exists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mJipinAlbumPath = Environment.getExternalStorageDirectory().getAbsolutePath() + LOCAL_ALBUM;
        } catch (Exception e) {
            e.printStackTrace();
            mJipinAlbumPath = null;
        }
    }

    /**
     * 保存数据
     * 
     * @param type
     * @param data
     * @return
     */
    public boolean saveData(int type, String filename, byte[] data) {
        if (type < 0 || type >= mCacheEnable.length) {
            return false;
        }
        boolean result = false;

        if (mCacheEnable[type]) {
            String path = mDiskCachePaths[type] + filename;
            result = FileUtil.saveFile(path, data);
        }
        return result;
    }

    /**
     * 读取数据
     * 
     * @param type
     * @return
     */
    public byte[] getData(int type, String filename) {
        if (type < 0 || type >= mCacheEnable.length) {
            return null;
        }
        byte[] result = null;

        if (mCacheEnable[type]) {
            String path = mDiskCachePaths[type] + filename;
            result = FileUtil.readFile(path);
        }
        return result;
    }

    /**
     * 保存图片
     * 
     * @param type
     * @param data
     * @return
     */
    public boolean saveImage(int type, String url, byte[] data) {
        if (type < 0 || type >= mCacheEnable.length) {
            return false;
        }
        boolean result = false;

        if (mCacheEnable[type]) {
            String path = mDiskCachePaths[type] + getCacheKey(url);
            result = FileUtil.saveFile(path, data);
            updateModTime(path);

            try {
                File file = new File(mDiskCachePaths[type]);
                if (file.exists()) {
                    int count = file.list().length;
                    if (count > DISK_CACHE_MAXS[type]) {
                        FileUtil.deleteOldestSubFile(mDiskCachePaths[type]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 读取图片
     * 
     * @param type
     * @param url
     * @return
     */
    public byte[] getImage(int type, String url) {
        if (type < 0 || type >= mCacheEnable.length) {
            return null;
        }
        byte[] result = null;

        if (mCacheEnable[type]) {
            String path = mDiskCachePaths[type] + getCacheKey(url);
            result = FileUtil.readFile(path);
        }
        return result;
    }

    public String getFullPath(String sLogo, int nType) {
        String sPath = mDiskCachePaths[nType] + getCacheKey(sLogo);

        return sPath;
    }

    private String getCacheKey(String url) {
        if (url == null) {
            throw new RuntimeException("Null url passed in");
        } else {
            return null/*StringUtil.md5(url)*/;
        }
    }

    private void updateModTime(String url) {
        try {
            File file = new File(url);
            file.setLastModified(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
