package com.yq.tjimagereadandshow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by v-yangqiang on 2016/4/6.
 */
public class ImageUtil {

    public final static int PHOTO_QUALITY = 92;

    public static Bitmap getNormalBitmap(String filePath, int width, int length) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, length);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            options.inSampleSize *= 2;
            try {
                bitmap = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
        }
        int degree = readPictureDegree(filePath);
        if (degree != 0) {
            bitmap = rotaingImageView(degree, bitmap);
        }

        return bitmap;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = null;
        // 创建新的图片
        try {
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizedBitmap;
    }

    /**
     * 根据服务器返回的压缩值和图片的长宽，进行压缩
     * @param filePath 存储路径
     * @param maxSize 屏幕长宽的最大值
     * @param compressValue 服务器返回的值
     * @return 根据服务器传回的CompressValue
     */
    public static Bitmap image_Compression_ByCompressValue(String filePath, int maxSize, int compressValue) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        //根据服务器返回的压缩值，设置不同的图片长宽
        if (compressValue == 2){  //获取图片长宽的最大值, 如果图片长宽最大值大于1920,将图片设置为1920
            maxSize = opts.outWidth > opts.outHeight ? opts.outWidth : opts.outHeight;
            if (maxSize > 1920){
                maxSize = 1920;
            }
        } else if (compressValue == 1){ //同上
            maxSize = opts.outWidth > opts.outHeight ? opts.outWidth : opts.outHeight;
            if (maxSize > 1600){
                maxSize = 1600;
            }
        } else if (compressValue == 0){ //根据屏幕长宽值的最大值，大于1280的，将按此比例进行压缩
            if (maxSize > 1280){
                maxSize = 1280;
            }
        }

        int widthRatio = (int) Math.ceil(opts.outWidth / maxSize);
        int heightRatio = (int) Math.ceil(opts.outHeight / maxSize);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }

        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        opts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath, opts);
            bmp = scaleImg(bmp, maxSize);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            opts.inSampleSize *= 2;
            bmp = BitmapFactory.decodeFile(filePath, opts);
            bmp = scaleImg(bmp, maxSize);
        }

        return bmp;
    }


    public static void compressBmpToFile(Bitmap bmp, String filePath, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Bitmap scaleImg(Bitmap img, float quality) {

        if (img == null) {
            return null;
        }
        float sHeight = 0;
        float sWidth = 0;

        Bitmap result = img;

        if (img.getWidth() >= img.getHeight() && img.getWidth() > quality) {

            sWidth = quality;
            sHeight = (quality * img.getHeight()) / img.getWidth();
            result = Bitmap.createScaledBitmap(img, (int) sWidth, (int) sHeight, true);

            if (null != img && !img.isRecycled() && !img.equals(result)) {
                img.recycle();
            }
        } else if (img.getHeight() > img.getWidth() && img.getHeight() > quality) {

            sHeight = quality;
            sWidth = (quality * img.getWidth()) / img.getHeight();
            result = Bitmap.createScaledBitmap(img, (int) sWidth, (int) sHeight, true);

            if (null != img && !img.isRecycled() && !img.equals(result)) {
                img.recycle();
            }
        }

        return result;
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 108, 192);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
}
