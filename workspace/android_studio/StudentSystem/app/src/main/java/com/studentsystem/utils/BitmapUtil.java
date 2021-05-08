package com.studentsystem.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b != null && b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static byte[] getBitmapFromFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            //获取到的图片太大了，需要裁减
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true; //先设置为true，只读取图片，不加载到内存中
            BitmapFactory.decodeFile(path, opts); // 这样，options里就得到了原图片的宽高值
            opts.inSampleSize = calculateInSampleSize(opts, 100, 100);
            opts.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);

            return bitmap2Bytes(bitmap);
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
