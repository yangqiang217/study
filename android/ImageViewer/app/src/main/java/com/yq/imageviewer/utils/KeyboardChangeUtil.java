package com.yq.imageviewer.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardChangeUtil {

    private int rootViewVisibleHeight;//纪录根视图的显示高度

    /**
     * 监听键盘高度变化
     */
    public void addViewChangeListener(final View rootView, final OnKeyboardChangeListener onKeyboardChangeListener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (onKeyboardChangeListener != null) {
                        onKeyboardChangeListener.onKeyboardShow();
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onKeyboardChangeListener != null) {
                        onKeyboardChangeListener.onKeyboardHide();
                    }
                    rootViewVisibleHeight = visibleHeight;
                }
            }
        });
    }

    public interface OnKeyboardChangeListener {
        void onKeyboardHide();

        void onKeyboardShow();
    }
}
