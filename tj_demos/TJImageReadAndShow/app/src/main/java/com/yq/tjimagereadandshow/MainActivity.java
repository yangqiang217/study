package com.yq.tjimagereadandshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    public static float density;
    public static int screenResolution;
    public static String screen;
    public static int heightPixels;
    public static int widthPixels;

    private ImageListView imageListView;

    private Context mContext;

    private ArrayList<String> mPicFilePaths = new ArrayList<String>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        setDensity();

        mPicFilePaths.add(getPath() + "20150616_155606.jpg");
        mPicFilePaths.add(getPath() + "20150618_180150.jpg");
        mPicFilePaths.add(getPath() + "20150618_191059.jpg");

        imageListView = (ImageListView) findViewById(R.id.imageListView);
        imageListView.setOnImageClickListener(new ImageClickListener());

        if (mPicFilePaths != null && !mPicFilePaths.isEmpty()) {
            List<Bitmap> list = getBitmapByFile(mPicFilePaths);
            imageListView.setMaxCount(list.size());
            imageListView.setCanEdited(false);
            imageListView.setAllBitmaps(list);
        }
    }

    //调整存放图片的数组，如果图片已经从sd卡中删除，这里需要从数组中删除
    private List<Bitmap> getBitmapByFile(ArrayList<String> fileArray) {
        List<Bitmap> list = new LinkedList<Bitmap>();
        for (String filepath : fileArray) {
            if (filepath != null && (new File(filepath).exists())) {
                list.add(BitmapManager.getInstance().getBitmap(filepath, false));
            }
        }

        return list;
    }

    private class ImageClickListener implements ImageListView.OnImageClickListener {

        @Override
        public void onImageClick (int index, boolean hasBitmap, ImageListView imageListView) {
            if (hasBitmap) {
                ImageShowActivity.show(mContext, mPicFilePaths.get(index));
            }
        }
    }

    public void setDensity() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density; // 屏幕密度（0.75 / 1.0 /// 1.5）
        screenResolution = metric.widthPixels * metric.heightPixels;
        screen = "" + metric.widthPixels + "*" + metric.heightPixels;
        heightPixels = metric.heightPixels;
        widthPixels = metric.widthPixels;
    }

    public String getPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return "";
        }
        return sdDir.toString() + "/DCIM/Camera/";
    }
}
