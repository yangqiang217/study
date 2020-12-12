package com.example.yq.mvvmdemo;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yq on 2017/2/20.
 */

public class ImageUtil {

    @BindingAdapter({"image"})
    public static void imageLoader(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }
}
