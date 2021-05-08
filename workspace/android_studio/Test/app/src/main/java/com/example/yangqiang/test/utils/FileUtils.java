package com.example.yangqiang.test.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.example.yangqiang.test.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by chengyuwang on 2016/8/15.
 */
public enum FileUtils {
    INSTANCE;

    public static final int VIDEO_CACHE_LIMIT = 100 * 1024 * 1024;

    // TODO: 2017/6/22 文件路径统一整理一下
    public enum Path {
        FILE, CACHE
    }

    FileUtils() {

    }

    public static void openAPK(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        // 使用FileProvider适配Android N之后的版本
        // 也不能全用新方法,测试在魅族Android6.0上,新方法报错ActivityNotFoundException
        Uri fileUri = getShareUri(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);
    }

    public static boolean createDirs(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static void clearCacheFiles() {
        File file = new File(FilePath.Cache.ROOT.path());
        try {
            if (file.exists()) {
                deleteFolder(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SD卡是否可用
     *
     * @return true->SD卡可用
     */
    public static boolean isSdcardAvaliable() {
        return Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED);
    }

    /**
     * SD卡空间是否足够 单位是b
     *
     * @param size 文件大小
     * @return true->空间充足
     */
    @SuppressWarnings("deprecation")
    public static boolean isAvaiableSpace(long size) {
        boolean isHasSpace = false;
        if (isSdcardAvaliable()) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = blocks * blockSize;
            if (availableSpare > size) {
                isHasSpace = true;
            }
        } else {
            return false;
        }
        return isHasSpace;
    }

    public static boolean exist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void deleteFolder(File file) {
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    deleteFolder(fileList[i]);
                } else {
                    fileList[i].delete();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long cacheLength() {
        File file = new File(FilePath.Cache.ROOT.path());
        long t1 = 0l;
        try {
            if (file.exists()) {
                t1 = getFolderSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t1;
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

//    public static void createTempDir() {
//        File file = new File(FilePath.Cache.DEFAULT.path());
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        File imageCacheDir = new File(FilePath.Cache.FRESCO.path());
//        if (!imageCacheDir.exists()) {
//            imageCacheDir.mkdirs();
//        }
//    }

    public static Uri getShareUri(File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(MyApplication.getInstance(), "com.sohu.youju.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private String cachedFileDir;
    private String cachedCacheDir;

    public String getFileDirStr() {
        if (!TextUtils.isEmpty(cachedFileDir)) {
            return cachedFileDir;
        }
        if (Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED)) {
            try {
                File file = MyApplication.getInstance().getExternalFilesDir(null);

                if (file != null) {
                    String fileDir = file.getAbsolutePath();
                    cachedFileDir = fileDir;
                    return fileDir;
                }
            } catch (Exception e) {

            }
        }
        String fileDir = MyApplication.getInstance().getFilesDir().getAbsolutePath();
        cachedFileDir = fileDir;
        return fileDir;
    }

    public String getDirStr(Path path) {
        switch (path) {
            case FILE:
            default:
                return getFileDirStr() + "youju";
            case CACHE:
                return getFileDirStr() + "cache";
        }
    }

    public String readFile(String path) {
        try {
            File f = new File(path);
            InputStream in = new FileInputStream(f);
            byte b[] = new byte[(int) f.length()];
            in.read(b);
            in.close();
            return new String(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void bufferSave(String msg, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(filePath, false));
            bfw.write(msg);
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public String readJsFromAssets(String path) {
        try {
            InputStream input = MyApplication.getInstance().getAssets().open(path);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
