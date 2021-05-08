package com.yq.uiautomatordemo;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangqiang on 2018/5/10.
 */
@RunWith(AndroidJUnit4.class)
public class UIAutoTestCase {

    private static final String LOG_TAG = "YQ_AUTOMATOR";

    private static final String PKG_DOUYIN = "com.ss.android.ugc.aweme";

    private static final int SHARE_X = 1350;
    private static final int SHARE_Y = 1840;

    private UiDevice mDevice;
    private UiSelector mUiSelector;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mUiSelector = new UiSelector();
    }

    @Test
    public void test() {
        try {
//            while (!exist(By.text("暂时没有更多了"))) {
//                mDevice.swipe(1000, 2200, 1000, 900, 10);
//            }
//            mDevice.click(230, 2000);
//            TimeUnit.SECONDS.sleep(2);
//            mDevice.swipe(1000, 2200, 1000, 900, 10);
//            TimeUnit.SECONDS.sleep(1);
//            mDevice.swipe(1000, 2200, 1000, 900, 10);
//            TimeUnit.SECONDS.sleep(1);

            int cost = doProcedure();
        } catch (Exception e) {
            e.printStackTrace();
            print("!!!!!!!!!!!!!!!!!!!!出现异常，重新开始!!!!!!!!!!!!");
        }
    }

    private int doProcedure() throws IOException, InterruptedException {
        print("-------------start new-------------------");
        long startTime = System.currentTimeMillis();
        print("clicking share");
        mDevice.click(SHARE_X, SHARE_Y);

        print("waitAndClick save");
        waitAndClick(By.text("保存本地"));
        print("click 800*600");
        mDevice.click(800, 600);
        TimeUnit.SECONDS.sleep(5);

        print("start swipe");
        mDevice.swipe(1000, 900, 1000, 2200, 10);
        TimeUnit.SECONDS.sleep(1);
        doProcedure();

        long endTime = System.currentTimeMillis();
        return (int) ((endTime - startTime) / 1000);
    }

    private boolean wait(BySelector bySelector, int timeout) {
        return mDevice.wait(Until.hasObject(bySelector), timeout);
    }

    private boolean waitAndClick(BySelector bySelector) {
        return waitAClickB(bySelector, bySelector);
    }

    private boolean waitAndClick(BySelector bySelector, int timeout) {
        return waitAClickB(bySelector, bySelector, timeout);
    }

    /**
     * @param waitTarget 等待的
     * @param clickTarget 点击的
     */
    private boolean waitAClickB(BySelector waitTarget, BySelector clickTarget) {
        return waitAClickB(waitTarget, clickTarget, 4000);
    }
    private boolean waitAClickB(BySelector waitTarget, BySelector clickTarget, int timeout) {
        boolean exist = mDevice.wait(Until.hasObject(waitTarget), timeout);
        if (exist) {
            mDevice.findObject(clickTarget).click();
        }
        return exist;
    }

    private void checkRetryTime(int retriedTime, int maxTime) {
        if (retriedTime == maxTime - 1) {
            throw new RuntimeException("retry timeout");
        }
    }

    private boolean exist(BySelector selector) {
        return mDevice.findObject(selector) != null;
    }


    private void print(long content) {
        print(String.valueOf(content));
    }
    private void print(double content) {
        print(String.valueOf(content));
    }
    private void print(float content) {
        print(String.valueOf(content));
    }
    private void print(int content) {
        print(String.valueOf(content));
    }
    private void print(boolean content) {
        print(String.valueOf(content));
    }
    private void print(String content) {
        Log.e(LOG_TAG, content);
    }
}
