package com.example.yangqiang.recyclerviewdragsort;

/**
 * Created by chengyuwang on 2016/8/18.
 */
public class ViewUtils {
    private static int ScreenHeight = -1;
    private static int ScreenWidth = -1;
    private static float itemWidth = -1F;

    private static Boolean hasNavBar = null;


    /**
     * 获取屏幕竖向长度, 该值大于屏幕宽度
     * @return
     */
    public static int getScreenHeight() {
        return 1920;
    }

    public static int getScreenWidth() {
        return 1080;
    }


}
