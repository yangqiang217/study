package com.yq.imageviewer.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.yq.imageviewer.MyApplication;
import com.yq.imageviewer.R;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author baoquanwang on 2016/7/12.
 */
public class DeviceUtils {

    private static String device_id = "";
    private static String device_type = null;
    private static String app_generated_id = "";

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void initDeviceType(Context context) {
        if (context != null) {
            try {
                PackageManager packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                device_type = "android-" + getAndroidVersion() + "/SNSProduct-" + packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getDeviceType() {
        if (TextUtils.isEmpty(device_type)) {
            initDeviceType(MyApplication.getAppContext());
        }
        if (TextUtils.isEmpty(device_type)) {
            return "ANDROID/UNKNOWN";
        } else {
            return device_type;
        }
    }


    private static String createDeviceUuid(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return null;
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            String tmDevice = "" + tm.getDeviceId();
            String tmSerial = "" + tm.getSimSerialNumber();
            String androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            Log.e("uuid", "uuid=" + uniqueId);
            return uniqueId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String getThirdOsVersion(String versionField) {
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            return (String) getMethod.invoke(classType, new Object[]{versionField});
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取顶部状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId <= 0) {
            resourceId = R.dimen.status_bar_height;
        }
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static final String VERSION_EMUI = "ro.build.version.emui";
    //    public static final String VERSION_MIUI_CODE = "ro.miui.ui.version.code";
//    public static final String VERSION_MIUI_NAME = "ro.miui.ui.version.name";

    public static final String BRAND_HUAWEI = "Huawei";
    public static final String BRAND_XIAOMI = "Xiaomi";
    public static final String BRAND_MEIZU = "Meizu";
}
