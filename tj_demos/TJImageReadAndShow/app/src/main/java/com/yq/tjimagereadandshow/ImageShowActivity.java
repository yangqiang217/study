
package com.yq.tjimagereadandshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class ImageShowActivity extends Activity {

    private ImageView mImg;
    public static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_image_show);
        initView();

        String mPicFPs = getIntent().getStringExtra(CODE);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapManager.getInstance().getPreviewBitmap(mPicFPs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
//            showToast("任务图片被损坏，请删除重做该任务");
//            finish();
            mImg.setImageResource(R.drawable.image_missed);
            return;
        }
        mImg.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        mImg.setImageBitmap(null);
        BitmapManager.getInstance().releasePreviewBitmap();
        super.onDestroy();
    }

    /**
     * View初始化
     */
    private void initView() {
        mImg = (ImageView) findViewById(R.id.img);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    /** 从给定路径加载图片 */
    private static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    private static long lastShowTime;

    public static void show(Context context, String filePath) {
        if (System.currentTimeMillis() - lastShowTime < 800) { // 防止短时间内（800毫秒）多次打开这个图片预览界面
            return;
        }
        lastShowTime = System.currentTimeMillis();

        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putExtra(CODE, filePath);
        context.startActivity(intent);
    }
}
