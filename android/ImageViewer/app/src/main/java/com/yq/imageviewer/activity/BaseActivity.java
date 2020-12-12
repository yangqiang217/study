package com.yq.imageviewer.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yq.imageviewer.view.swipeback.SwipeBackActivityBase;
import com.yq.imageviewer.view.swipeback.SwipeBackActivityHelper;
import com.yq.imageviewer.view.swipeback.SwipeBackLayout;
import com.yq.imageviewer.view.swipeback.SwipeBackUtils;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isSwipeBackEnable()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeBackEnable()) {
            mHelper.onPostCreate();
        }
    }

    protected final void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否支持仿IOS滑动返回上一页的效果
     */
    public boolean isSwipeBackEnable() {
        return true;
    }

    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    @Override
    public final SwipeBackLayout getSwipeBackLayout() {
        if (isSwipeBackEnable()) {
            return mHelper.getSwipeBackLayout();
        } else {
            return null;
        }
    }

    /**
     * use {@link #isSwipeBackEnable()} instead
     */
    @Deprecated
    @Override
    public final void setSwipeBackEnable(boolean enable) {
        if (getSwipeBackLayout() != null) {
            getSwipeBackLayout().setEnableGesture(isSwipeBackEnable());
        }
    }

    /**
     * 主动调用改方法取代finish，仿IOS右滑返回上一页
     * Scroll out contentView and finish the activity
     */
    @Override
    public final void scrollToFinishActivity() {
        if (getSwipeBackLayout() != null) {
            SwipeBackUtils.convertActivityToTranslucent(this);
            getSwipeBackLayout().scrollToFinishActivity();
        }
    }
}
