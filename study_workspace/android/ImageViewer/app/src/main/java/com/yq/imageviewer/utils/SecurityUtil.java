package com.yq.imageviewer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.yq.imageviewer.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtil {

    public final static int ALL = 1000;
    public final static int WRITE_EXTERNAL_STORAGE = 1001;
    public final static int CAMERA = 1002;
    public final static int ACCESS_FINE_LOCATION = 1003;
    public final static int READ_PHONE_STATE = 1004;

    public static boolean needCheckPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void requestPermission(Context context,
        String permission, onPermissionListener listener) {
        if (needCheckPermission() && ContextCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission},
                    getPermissionRequestCode(permission));
            }
        } else {
            if (listener != null) {
                listener.onGranted();
            }
        }
    }

    public static void requestPermissions(Activity activity, String permissions[], onPermissionListener listener) {
        if (needCheckPermission()) {
            List<String> permissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (permissionList.size() > 0) {
                ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]), ALL);
                return;
            }
        }
        if (listener != null) {
            listener.onGranted();
        }
    }

    private static int getPermissionRequestCode(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return CAMERA;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return WRITE_EXTERNAL_STORAGE;

            case Manifest.permission.ACCESS_FINE_LOCATION:
                return ACCESS_FINE_LOCATION;

            case Manifest.permission.READ_PHONE_STATE:
                return READ_PHONE_STATE;

            default:
                return 0;
        }
    }

    public static void getAppDetailSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    public static boolean hasGEOPermission() {
        return SecurityUtil.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            || SecurityUtil.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean hasPermission(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return false;
        }
        return ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), permission)
            == PackageManager.PERMISSION_GRANTED;
    }

    public interface onPermissionListener {
        void onGranted();

        void onRefused();
    }

}
