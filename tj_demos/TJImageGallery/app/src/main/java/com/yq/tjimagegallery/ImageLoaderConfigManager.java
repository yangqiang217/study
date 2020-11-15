package com.yq.tjimagegallery;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * ImageLoader初始化，图片显示方式 管理类
 */
public class ImageLoaderConfigManager {
    /**
     * 初始化ImagerLoader,调用一次
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        if (ImageLoader.getInstance().isInited()) {
            return;
        }
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2); //下载线程优先级
        config.denyCacheImageMultipleSizesInMemory(); //是否允许缓存不同size的某张图片
        config.discCacheFileNameGenerator(new Md5FileNameGenerator()); //MD5方式缓存文件名称
        config.discCacheSize(50 * 1024 * 1024); //缓存最大值50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO); //设置任务执行顺序
        config.writeDebugLogs(); //打log Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
