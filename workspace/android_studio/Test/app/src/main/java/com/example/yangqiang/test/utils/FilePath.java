package com.example.yangqiang.test.utils;

import android.os.Environment;

import java.util.concurrent.TimeUnit;

/**
 * Created by baipingwei on 2017/11/9.
 */

public class FilePath {
    /**
     * app外部存储，卸载应用依然存在
     */
    public enum External {
        DEFAULT("/default/"), PIC("/pic/");

        private String path = "";

        External(String path) {
            this.path = path;
        }

        public String path() {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/youju/" + path;
            FileUtils.createDirs(filePath);
            return filePath;
        }

        public boolean exsitFile(String filePath) {
            java.io.File file = new java.io.File(filePath);
            return file.exists();
        }
    }

    /**
     * app外部存储，
     * 卸载应用不存在
     * 不可清理
     */
    public enum File {
        DEFAULT("/default/"), RETROFIT("/retrofit/"), BACKUP("/backup/");

        private String path;

        File(String path) {
            this.path = path;
        }

        public String path() {
            String filePath = FileUtils.INSTANCE.getFileDirStr() + "/file/" + path;
            FileUtils.createDirs(filePath);
            return filePath;
        }
    }

    public enum SaveDid {
        SYSTEM("/system/"), ANDROID("/Android/"), ROOT("/"), SELF_BUILD("/syss/");

        private String path;

        SaveDid(String path) {
            this.path = path;
        }

        public String path() {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
            FileUtils.createDirs(filePath);
            return filePath;
        }
    }

    /**
     * app外部存储，
     * 卸载应用不存在
     * 清理缓存
     */
    public enum Cache {
        ROOT(""), DEFAULT("/default/"), FRESCO("/fresco/"), FRESCO_SMALL("/fresco_small/"), WEB("/web/"), VIDEO("/video/"), SOFA_VIDEO("/sofa_video/");

        private String path;

        Cache(String path) {
            this.path = path;
        }

        public String path() {
            String filePath = FileUtils.INSTANCE.getFileDirStr() + "/cache/" + path;
            FileUtils.createDirs(filePath);
            return filePath;
        }
    }

    public void job() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("jobb");
                }
            }
        }).start();
    }
}
