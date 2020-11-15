package com.yq.hooker.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by yangqiang on 2018/5/23.
 */

public class PermissionUtil {


    public final static int ALL = 1000;
    public final static int WRITE_EXTERNAL_STORAGE = 1001;
    public final static int CAMERA = 1002;
    public final static int ACCESS_FINE_LOCATION = 1003;
    public final static int READ_PHONE_STATE = 1004;
    public final static int READ_EXTERNAL_STORAGE = 1005;

    private static boolean needCheckPermission() {
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
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) { }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }


    public interface onPermissionListener {
        void onGranted();

        void onRefused();
    }
}
