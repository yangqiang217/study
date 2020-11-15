package com.yq.tjimagereadandshow;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by v-yangqiang on 2016/4/6.
 */
public class PhotoUtil {

    public static int COMPRESS_VALUE = 0;

    /**
     * 图片压缩并返还当前压缩的bitmap, 并且原图文件进行了压缩
     * @author yunfei.duan
     * filePath: 文件路径
     * isNavPic: 是否对图片质量进行压缩， false,不压缩;true,压缩
     */
    public static void compresImageByBitmap(Activity activity, String filePath, boolean isNavPic) {
        int degree = ImageUtil.readPictureDegree(filePath);  //获取当前图片的角度
        int index = MainActivity.widthPixels > MainActivity.heightPixels ? MainActivity.widthPixels
                : MainActivity.heightPixels;   //获取屏幕长宽的最大值
        int quality = ImageUtil.PHOTO_QUALITY; //初始化照片质量
        int compressValue = COMPRESS_VALUE;  //压缩值，此值是后台服务器传过来，0、1、2三个值
        //假如传的是错误数据，直接设置为默认质量
        if (compressValue>2 || compressValue<0){
            compressValue = 0;
        }
        //判断是否对图片质量进行压缩
        if (!isNavPic){
            quality = ImageUtil.PHOTO_QUALITY;
        } else {
            //如果不需要对图片质量进行压缩，质量为100
            quality = 100;
        }
        //对图片的长宽进行压缩,并且对图片进行旋转
        Bitmap newbitmap = ImageUtil.rotaingImageView(degree,
                ImageUtil.image_Compression_ByCompressValue(filePath, index, compressValue));
        // 将旋转，长宽处理后的图片存入文件
        ImageUtil.compressBmpToFile(newbitmap, filePath, quality);
        //保存完后，释放内存中的图片
        newbitmap.recycle();
    }
}
