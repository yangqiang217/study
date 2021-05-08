package com.example.yangqiang.test.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class BitmapCompress {

    /**
     * 质量压缩
     * 质量压缩不会减少图片的像素，它是在保持像素的前提下改变图片的位深及透明度，来达到压缩图片的目的，
     * 图片的长，宽，像素都不会改变，那么bitmap所占内存大小是不会变的。
     * 我们可以看到有个参数：quality，可以调节你压缩的比例，但是还要注意一点就是，
     * 质量压缩堆png格式这种图片没有作用，因为png是无损压缩。
     */
    public static Bitmap compressQuality(Bitmap bm) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 10, bos);
        byte[] bytes = bos.toByteArray();
        Bitmap res = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.d("bitmapcompress", "compressQuality: " + res.getAllocationByteCount());
        return res;
    }

    /**
     * 采样率压缩
     * 采样率压缩其原理其实也是缩放bitamp的尺寸，通过调节其inSampleSize参数，比如调节为2，宽高会为原来的1/2，内存变回原来的1/4.
     */
    public static Bitmap compressSampling(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap res = BitmapFactory.decodeFile(path, options);
        Log.d("bitmapcompress", "compressSampling: " + res.getAllocationByteCount());
        return res;
    }

    /**
     * 缩放法压缩
     * 放缩法压缩使用的是通过矩阵对图片进行裁剪，也是通过缩放图片尺寸，来达到压缩图片的效果，和采样率的原理一样。
     */
    public static Bitmap compressMatrix(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap res = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        Log.d("bitmapcompress", "compressMatrix: " + res.getAllocationByteCount());
        return res;
    }

    /**
     * RGB_565压缩
     * 这是通过压缩像素占用的内存来达到压缩的效果，一般不建议使用ARGB_4444，因为画质实在是辣鸡，
     * 如果对透明度没有要求，建议可以改成RGB_565，相比ARGB_8888将节省一半的内存开销。
     */
    public static Bitmap compressRGB565(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap b = BitmapFactory.decodeFile(path, options);
        Log.d("bitmapcompress", "compressRGB565: " + b.getAllocationByteCount());
        return b;
    }

    /**
     * createScaledBitmap
     * 将图片的大小压缩成用户的期望大小，来减少占用内存。
     */
    public static Bitmap compressScaleBitmap(Bitmap bm, int dstW, int dstH) {
        Bitmap res =  Bitmap.createScaledBitmap(bm, dstW, dstH, true);
        Log.d("bitmapcompress", "compressScaleBitmap: " + res.getAllocationByteCount());
        return res;
    }
}
