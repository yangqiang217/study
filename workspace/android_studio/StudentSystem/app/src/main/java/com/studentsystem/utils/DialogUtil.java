package com.studentsystem.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * 简单封装dialog
 */
public class DialogUtil {

    /**
     * 只有一个确认按钮的
     * @param confirm 确认按钮文案
     */
    public static void showDialog(Context contex, String message, String confirm,
        final DialogInterface.OnClickListener confirmListener) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(contex)
            .setMessage(message)
            .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (confirmListener != null) {
                        confirmListener.onClick(dialogInterface, i);
                    }
                }
            });
        builder.create().show();
    }

    /**
     * 有确认、取消两个按钮的
     * @param confirm 确认按钮文案
     * @param cancel 取消按钮文案
     */
    public static void showDialog(Context context, String message, String confirm, String cancel,
        final DialogInterface.OnClickListener confirmListener) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (confirmListener != null) {
                        confirmListener.onClick(dialogInterface, i);
                    }
                }
            }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        builder.create().show();
    }

    /**
     * 方便调用的两个按钮的，免得每次调用都要写确认取消
     */
    public static void showDialog(Context context, String message, final DialogInterface.OnClickListener confirmListener) {
        showDialog(context, message, "确认", "取消", confirmListener);
    }
}
