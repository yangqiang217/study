package com.example.qiangyang.verticalviewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangyang on 2017/4/26.
 */

public class ImageUriUtils {

    private static int index = 0;
    private static List<String> list = new ArrayList<>();

    public static String getImageUrl() {
        if (index >= list.size()) {
            index = 0;
        }
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493218729462&di=63caec810180cbd6b32a1e0d9f10a237&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F033f9c3554c7d7c00000158fcedb639.jpg   ");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220089004&di=0e109e05447814426cc6eab47ddc9cd9&imgtype=0&src=http%3A%2F%2Fpic.sucaiw.com%2Fup_files%2Fbizhi%2F61d2c4ccb4%2Fsucaiw-zmbz6843100tys.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220102692&di=02365f5c023c7ac5b18b3a3a8d1727da&imgtype=0&src=http%3A%2F%2Fimg2.pconline.com.cn%2Fpconline%2F0711%2F05%2F1147223_071105view29.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220116397&di=d3bc48f2cb2cd17ff3cabc70102936f3&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1209%2F13%2Fc0%2F13827288_1347505315557.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220132266&di=5b9e9892b533135f76cae5c3293274bc&imgtype=0&src=http%3A%2F%2Ffile28.mafengwo.net%2FM00%2F39%2F35%2FwKgB6lT8Nk6AesJfAAp9EHHNQUk29.jpeg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220146593&di=02cd8eabcc2737acf375bf4734a68477&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F573444af4d7aa.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493814891&di=40f76420f4b80716e754b25390084781&imgtype=jpg&er=1&src=http%3A%2F%2Fatt.x2.hiapk.com%2Fforum%2Fmonth_1011%2F1011272006c0bfb98d8246598a.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220219055&di=48f88b4905c61cfc35b46c62b87a4ecd&imgtype=0&src=http%3A%2F%2Fwww.wallcoo.com%2Fnature%2FAmazing_Color_Landscape_2560x1600%2Fwallpapers%2F2560x1600%2FAmazing_Landscape_38_II.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493221123145&di=a163e2f34ee832e6f576953a1dda86c6&imgtype=0&src=http%3A%2F%2Fyouimg1.c-ctrip.com%2Ftarget%2Ftg%2F437%2F683%2F176%2F511b0e869c364b77b0cf030722a11eac.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220247363&di=0c81cf8afc6e5e86574089812f04e6b8&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2015%2Fsaraxuss%2F04%2F08%2Ffj%2F2%2F10.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493220263687&di=ff2404c98be269b08695a3994e37253d&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201606%2F24%2F224816tccnnca2s81ezo3q.jpg");

        return list.get(index++);
    }
}
