package com.example.yangqiang.test.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 字符串格式相关
 * Created by baipingwei on 2016/12/23.
 */

public class StringUtils {

    public static String emptyIfNull(String string) {
        return string == null ? "" : string;
    }

    /**
     * 按照format的格式要求,返回时间字符串
     * format格式举例:
     * yyyy-MM-dd HH:mm:ss
     *
     * @param format
     * @param timestamp
     * @return
     */
    public static String timestampToDate(String format, long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取 GMT 时间
     * @return
     */
    public static String getGMTTimeStr(long timestamp) {
        Date d = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(d);
    }

    public static String getDurationStr(long millisecond) {
        int totalSeconds = ((int) millisecond / 1000);
        int minute = totalSeconds / 60;
        int second = totalSeconds % 60;
        return (minute > 0 ? String.valueOf(minute) + "' " : "") + String.valueOf(second) + "\"";
    }

    public static String getDurationStrInMinutes(long millisecond) {
        int minute = (int) millisecond / 1000 / 60;
        return minute + "分钟";
    }

    public static String getFullDurationStr(long millisecond) {
        if (millisecond < 0) return "00:00";
        int totalSeconds = ((int) millisecond / 1000);
        int minute = totalSeconds / 60;
        int second = totalSeconds % 60;
        int hour = minute / 60;
        minute = minute % 60;
        return (hour > 0 ? String.valueOf(hour) + ":" : "") +
                (minute < 10 ? "0" : "") + String.valueOf(minute) + ":" +
                (second < 10 ? "0" : "") + String.valueOf(second) + "";
    }

    public static String getCountdownTimeStr(long millisecond) {
        return getCountdownTimeStr("hh:mm:ss", millisecond);
    }

    /**
     * @param format hh:mm:ss或mm:ss(小时换算到分)或ss(小时、分都换算成秒)
     *               或hh(不要分秒)或hh:mm(不要秒)
     */
    public static String getCountdownTimeStr(String format, long millisecond) {
        if (millisecond < 0 || TextUtils.isEmpty(format)) {
            return format;
        }

        if (format.contains("hh")) {
            int totalSeconds = ((int) millisecond / 1000);
            int minute = totalSeconds / 60;
            int second = totalSeconds % 60;
            int hour = minute / 60;
            minute = minute % 60;
            String res = String.format(Locale.CHINA, "%02d", hour);
            if (format.contains("mm")) {
                res += ":" + String.format(Locale.CHINA, "%02d", minute);
            }
            if (format.contains("ss")) {
                res += ":" + String.format(Locale.CHINA, "%02d", second);
            }
            return res;
        } else if (format.contains("mm")) {
            int totalSeconds = ((int) millisecond / 1000);
            int minute = totalSeconds / 60;
            int second = totalSeconds % 60;
            String res = String.format(Locale.CHINA, "%02d", minute);
            if (format.contains("ss")) {
                res += ":" + String.format(Locale.CHINA, "%02d", second);
            }
            return res;
        } else if (format.contains("ss")) {
            return String.format(Locale.CHINA, "%02d", ((int) millisecond / 1000));
        } else {
            return null;
        }
    }

    public static String getPraiseCount(long count) {
        if (count < 0) return "0";
        if (count < 10000) return String.valueOf(count);
        return "1万+";
    }

    public static String getCountStr(long count) {
        if (count < 0) return "0";
        if (count < 10000) return String.valueOf(count);
        long num = count / 10000;
        return num + "万+";
    }

    public static String getCommentCountStr(long count) {
        if (count < 0) return "0";
        if (count < 10_000) {
            return count + "";
        }
        if (count >= 100_000_000) {
            return count / 100_000_000 + "亿";
        } else {
            return count / 10_000 + "万";
        }
    }

    public static String getFileSize(long byteSize) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");

        if ((double) byteSize <= 0) {
            return 0 + "B";
        } else if ((double) byteSize < 1024) {
            return df.format((double) byteSize) + "B";
        } else if ((double) byteSize < 1024 * 1024) {
            return df.format((double) byteSize / 1024) + "KB";
        } else if ((double) byteSize < 1024 * 1024 * 1024) {
            return df.format((double) byteSize / 1024 / 1024) + "MB";
        } else {
            return df.format((double) byteSize / 1024 / 1024 / 1024) + "GB";
        }
    }

    /**
     * url使用utf-8编码，并去除里面的空格、汉字等
     *
     * @param url
     * @return
     */
    public static String formatUrl(String url) {
        if (url == null) return null;
        try {
            url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    //判读是否包含表情
    public static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //以下只能过滤部分表情符号
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
}
