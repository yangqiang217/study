package com.yq.imageviewer;

import android.os.Environment;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class Const {

    public static final int COLUMN = 3;

    public static final String URL_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/urls.txt";
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/.sysc/";
    public static final String DOWNLOAD_TEMP_NAME = "0000000000";
    public static final String DOWNLOAD_TEMP_PATH = PATH + DOWNLOAD_TEMP_NAME + "/";

    /** 文件夹名是标题_发布时间 */
    public static final String GAP_TITLE_TIME = "_";
    /** 年月日间隔 */
    public static final String GAP_TIME = "-";

    /** 图片后缀名 */
    public static final String FILE_END = ".kmp";

}
