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

    private static final int REPEAT_COUNT = 10;

    private static final String PKG_SHAFA = "com.sohu.youju";
    private static final String PKG_INFOHOOK = "com.yq.infohook";
    private static final String PKG_YINGYONGBAO = "com.tencent.android.qqdownloader";
    private static final String PKG_CHROME = "com.android.chrome";

    private static final int TIMEOUT = 10 * 1000;
    private static final int TIMEOUT_SHORT = 5000;

    private static final int RETRY_TIME = 5;

    private UiDevice mDevice;
    private UiSelector mUiSelector;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mUiSelector = new UiSelector();
    }

    @Test
    public void test() {
        for (int i = 0; i < REPEAT_COUNT; i++) {
            print("-------------round: " + i + " start-------------------");
            try {
                int cost = doProcedure();
                print("-------------round " + i + " cost: " + cost);
            } catch (Exception e) {
                e.printStackTrace();
                print("!!!!!!!!!!!!!!!!!!!!出现异常，重新开始!!!!!!!!!!!!");
            }
        }
    }

    private int doProcedure() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        mDevice.pressHome();

        //卸载沙发
        mDevice.executeShellCommand("pm uninstall " + PKG_SHAFA);
        print("卸载沙发");

        //清除应用宝数据
        mDevice.executeShellCommand("pm clear " + PKG_YINGYONGBAO);
        print("清空应用宝数据");

        mDevice.pressHome();
        //清除chome数据
        String clearChrome = mDevice.executeShellCommand("pm clear " + PKG_CHROME);
        print("清除chome数据：" + clearChrome);

        //切到hook
        mDevice.findObject(By.desc("Hooker")).click();
        print("切到hook");
        //清空sd卡
        waitAndClick(By.text("CLEAR_SDCARD"));
        print("清空sd卡");
        for (int i = 0; i < RETRY_TIME; i++) {
            if (mDevice.wait(Until.hasObject(By.text("clear success")), TIMEOUT)) {
                break;
            }
            checkRetryTime(i, RETRY_TIME);
            print("重试清空sd卡");
            waitAndClick(By.text("CLEAR_SDCARD"));
        }
        //往sd卡写信息
        waitAndClick(By.text("WRITE_RANDOM_INFO"));
        print("往sd卡写信息");
        for (int i = 0; i < RETRY_TIME; i++) {
            if (mDevice.wait(Until.hasObject(By.text("write success")), TIMEOUT)) {
                break;
            }
            checkRetryTime(i, RETRY_TIME);
            print("重试往sd卡写信息");
            waitAndClick(By.text("WRITE_RANDOM_INFO"));
        }

//        //切换代理
//        print("start change proxy");
//        mDevice.executeShellCommand("settings put global http_proxy 10.2.155.64:8888");

        //打开应用宝
        mDevice.pressHome();
        mDevice.findObject(By.desc("应用宝")).click();
        print("open 应用宝");

        //可能出现推荐动画或者默认安装界面
        boolean startClicked = false;
        while (!startClicked) {
            if (exist(By.text("开始新搜索"))) {
                print("跳过动画");
                mDevice.click(950, 180);
                startClicked = true;
            } else if (exist(By.text("取消全选"))) {
                print("跳过推荐安装");
                mDevice.click(68, 135);
                startClicked = true;
            }
        }

        //点击搜索
        mDevice.wait(Until.hasObject(By.text("首页")), TIMEOUT);
        print("点击搜索");
        mDevice.click(600, 140);

        //位置权限
        mDevice.wait(Until.hasObject(By.text("获取权限提示")), TIMEOUT);
        print("取消位置授权");
        waitAndClick(By.text("取消"));

        //输入
        for (int i = 0; i < RETRY_TIME; i++) {
            if (mDevice.wait(Until.hasObject(By.text("搜索")), TIMEOUT)) {
                break;
            }
            checkRetryTime(i, RETRY_TIME);
            print("重新等待搜索按钮");
        }
        for (int i = 0; i < RETRY_TIME; i++) {
            if (mDevice.wait(Until.hasObject(By.clazz(EditText.class)), TIMEOUT)) {
                break;
            }
            checkRetryTime(i, RETRY_TIME);
            print("重新等待输入框");
        }

        mDevice.findObject(By.clazz(EditText.class)).setText("沙发视频");
        print("输入沙发视频");

        print("输入后点击搜索");
        waitAndClick(By.text("搜索"), TIMEOUT_SHORT);
        //下载
        for (int i = 0; i < RETRY_TIME * 2; i++) {
            if (waitAndClick(By.clazz(TextView.class).text("沙发视频"))) {
                break;
            }
            checkRetryTime(i, RETRY_TIME * 2);
            print("重新加载列表: " + i);
            waitAndClick(By.text("搜索"), TIMEOUT_SHORT);
        }

        for (int i = 0; i < RETRY_TIME * 2; i++) {
            if (mDevice.wait(Until.hasObject(By.text("详情")), TIMEOUT)) {
                break;
            }
            checkRetryTime(i, RETRY_TIME * 2);
            waitAndClick(By.clazz(TextView.class).text("沙发视频"));
            waitAndClick(By.text("重新加载"));
            print("重试进详情页");
        }

        mDevice.click(500, 1850);
        print("下载");
        //安装
        for (int i = 0; i < RETRY_TIME; i++) {
            if (mDevice.wait(Until.hasObject(By.textContains("您要安装此应用吗")), TIMEOUT)) {
                break;
            }
            print("重新下载: " + i);
            checkRetryTime(i, RETRY_TIME);
            mDevice.click(500, 1850);
        }

        for (int i = 0; i < RETRY_TIME; i++) {
            if (waitAndClick(By.text("安装"))) {
                print("安装");
                break;
            }
            print("重试安装: " +i);
            checkRetryTime(i, RETRY_TIME);
        }

        //打开沙发视频
        for (int i = 0; i < RETRY_TIME; i++) {
            if (waitAndClick(By.text("打开"))) {
                print("打开沙发视频");
                break;
            }
            print("重试打开: " + i);
            checkRetryTime(i, RETRY_TIME);
        }

        //权限
        for (int i = 0; i < RETRY_TIME; i++) {
            if (waitAndClick(By.text("始终允许"))) {
                print("权限");
                break;
            }
            print("重试允许: " + i);
            checkRetryTime(i, RETRY_TIME);
        }

        TimeUnit.SECONDS.sleep(1);

        //关闭拆红包
        for (int i = 0; i < RETRY_TIME; i++) {
            if (waitAndClick(By.res(PKG_SHAFA, "close_new_man_tip"))) {
                print("关闭拆红包");
                break;
            }
            print("重试关闭拆红包: " + i);
            checkRetryTime(i, RETRY_TIME);
        }
        //关闭金币弹框
//        print("关闭金币弹框");
//        TimeUnit.SECONDS.sleep(1);
//        mDevice.click(500, 500);
        //播放第一个
        TimeUnit.SECONDS.sleep(1);
        print("播放第一个");
        mDevice.click(550, 480);

        TimeUnit.SECONDS.sleep(3);

        long endTime = System.currentTimeMillis();
        return (int) ((endTime - startTime) / 1000);
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
        return waitAClickB(waitTarget, clickTarget, TIMEOUT);
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
