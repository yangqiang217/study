# -*- coding: utf-8 -*-
import uiautomator2 as u2
import time
import os

TIMEOUT = 30.0
TIMEOUT_SHORT = 5.0
TIMEOUT_LONG = 1800.0

RETRY_TIME = 3


class TimeoutException(Exception):
    def __init__(self, ErrorInfo):
        super(TimeoutException, self).__init__(self)
        self.errorinfo = ErrorInfo

    def __str__(self):
        return self.errorinfo


def checkRetryTime(retriedTime, maxTime):
    if retriedTime == maxTime - 1:
        raise TimeoutException("retry timeout")


def checkTimeout(startTime):
    elapse = (int)(time.time() - startTime)
    if (elapse >= TIMEOUT):
        raise TimeoutException("while loop timeout")


def doProcedure():
    startTime = time.time()
    d = u2.connect('10.2.155.209')
    print("设备已连接")
    # d.debug = True
    # d.healthcheck()

    d.press("home")
    # 卸载沙发
    d.app_uninstall('com.sohu.youju')
    print("卸载沙发")

    # 清除应用宝数据
    d.app_clear('com.tencent.android.qqdownloader')
    print("清空应用宝数据")
    d.press("home")

    # 清除chome数据
    d.app_clear('com.android.chrome')
    print("清空chrome数据")

    # 切到hook=
    d(text="Hooker").click(timeout=TIMEOUT)
    print("切到hook")
    # 清空sd卡
    d(text="CLEAR_SDCARD").click_exists(timeout=TIMEOUT)
    print("清空sd卡")
    for i in range(0, RETRY_TIME):
        if d(text="clear success").wait(timeout=TIMEOUT):  # return bool
            break
        checkRetryTime(i, RETRY_TIME)
        print("重试清空sd卡")
        d(text="CLEAR_SDCARD").click_exists(timeout=TIMEOUT)

    # 往sd卡写信息
    d(text="WRITE_RANDOM_INFO").click_exists(timeout=TIMEOUT)
    print("往sd卡写信息")
    for i in range(0, RETRY_TIME):
        if d(text="write success").wait(timeout=TIMEOUT):  # return bool
            break
        checkRetryTime(i, RETRY_TIME)
        print("重试往sd卡写信息")
        d(text="WRITE_RANDOM_INFO").click_exists(timeout=TIMEOUT)

    # 打开应用宝
    d.press("home")
    d(text="应用宝").click()
    print("打开应用宝")

    # 权限
    print("权限")
    for i in range(0, RETRY_TIME):
        if d(text="去授权").click_exists(timeout=TIMEOUT):
            break
        checkRetryTime(i, RETRY_TIME)
        print("重新点击去授权")

    permission1 = d(text="允许").click_exists(timeout=TIMEOUT)
    permission2 = d(text="允许").click_exists(timeout=TIMEOUT_SHORT)
    permission3 = d(text="允许").click_exists(timeout=TIMEOUT_SHORT)
    print(str(permission1) + ", " + str(permission2) + ", " + str(permission3))
    needshowyingyongbaopermission = not (permission1 and permission2 and permission3)
    if needshowyingyongbaopermission:
        print("仍需后续权限")

    # 可能出现推荐动画或者默认安装界面
    startClicked = False
    recommendStartTime = time.time()
    while not startClicked:
        checkTimeout(recommendStartTime)
        if d.exists(text="开始新搜索"):
            print("跳过动画")
            d.click(950, 180)
            startClicked = True
        elif d.exists(text="取消全选"):
            print("跳过推荐安装")
            d.click(68, 135)
            startClicked = True
        elif d.exists(text="装机必备"):
            print("跳过推荐安装")
            d.click(1000, 135)
            startClicked = True

    # 点击搜索
    d(text="首页").wait(timeout=TIMEOUT)
    print("点击搜索")
    d.click(600, 140)

    if needshowyingyongbaopermission:
        for i in range(0, RETRY_TIME):
            if d(text="获取权限提示").wait(timeout=TIMEOUT):
                print("取消位置授权")
                d(text='取消').click_exists(timeout=1.0)
                break
            checkRetryTime(i, RETRY_TIME)
            print("重新等待后续出现的位置授权")

    # 输入
    for i in range(0, RETRY_TIME):
        if d(text="搜索").wait(timeout=TIMEOUT):
            break
        checkRetryTime(i, RETRY_TIME)
        print("重新等待搜索按钮")
    for i in range(0, RETRY_TIME):
        if d(className="android.widget.EditText").wait(timeout=TIMEOUT):
            break
        checkRetryTime(i, RETRY_TIME)
        print("重新等待输入框")

    inputshafa = d(className="android.widget.EditText").set_text("沙发视频")
    print("输入沙发视频: " + str(inputshafa))

    print("输入后点击搜索")
    d(text="搜索").click_exists(timeout=TIMEOUT_SHORT)
    # 下载
    for i in range(0, RETRY_TIME):
        if d(text='沙发视频', className='android.widget.TextView').click_exists(timeout=TIMEOUT):
            break
        checkRetryTime(i, RETRY_TIME)
        print("重新加载列表: " + str(i))
        d(text="搜索").click_exists(timeout=TIMEOUT_SHORT)

    for i in range(0, RETRY_TIME):
        if d(text="详情").wait(timeout=TIMEOUT):
            break
        checkRetryTime(i, RETRY_TIME)
        d(text='沙发视频', className='android.widget.TextView').click_exists(timeout=1)
        d(text='重新加载').click_exists(timeout=TIMEOUT)
        print("重试进详情页")

    d.click(500, 1850)
    print("下载")
    # 安装
    for i in range(0, RETRY_TIME):
        if d(textContains="您要安装此应用吗").wait(timeout=TIMEOUT):
            break
        print("重新下载: " + str(i))
        checkRetryTime(i, RETRY_TIME)
        d.click(500, 1850)

    for i in range(0, RETRY_TIME):
        if d(text="安装").click_exists():
            print("安装")
            break
        print("重试安装: " + str(i))
        checkRetryTime(i, RETRY_TIME)

    # 打开沙发视频
    for i in range(0, RETRY_TIME):
        if d(text="打开").click_exists(timeout=TIMEOUT):
            print("打开沙发视频")
            break
        print("重试打开: " + str(i))
        checkRetryTime(i, RETRY_TIME)

    # 权限
    for i in range(0, RETRY_TIME):
        if d(text="允许").click_exists(timeout=TIMEOUT):
            print("权限")
            break
        print("重试允许: " + str(i))
        if d.exists(text="打开"):
            d(text="打开").click(timeout=TIMEOUT)
        checkRetryTime(i, RETRY_TIME)
    for i in range(0, RETRY_TIME):
        if d(text="允许").click_exists(timeout=TIMEOUT):
            print("权限")
            break
        print("重试允许: " + str(i))
        checkRetryTime(i, RETRY_TIME)
    d(text="允许").click_exists(timeout=TIMEOUT)
    time.sleep(1)
    # while not d(text="允许").click_exists(timeout=TIMEOUT):
    #     print("重试允许2")
    # time.sleep(1)
    # while not d(text="允许").click_exists(timeout=TIMEOUT):
    #     print("重试允许3")

    # 关闭拆红包
    for i in range(0, RETRY_TIME):
        if d(resourceId="com.sohu.youju:id/close_new_man_tip").click_exists(timeout=TIMEOUT):
            print("关闭拆红包")
            break
        print("重试关闭拆红包: " + str(i))
        checkRetryTime(i, RETRY_TIME)
    # d.click(933, 528)
    # 关闭金币弹框
    # time.sleep(1)
    # d.click(500, 500)
    print("关闭金币弹框")
    # 播放第一个
    print("播放第一个")
    d.click(550, 480)
    time.sleep(3)

    endTime = time.time()
    return int(endTime - startTime)


if __name__ == '__main__':
    for i in range(1, 1000000):
        print("-------------round " + str(i) + " start--------------")
        try:
            cost = doProcedure()
            print("-------------round " + str(i) + " cost: " + str(cost))
        except TimeoutException, e:
            print(e.message)
        except BaseException, e:
            print(e.message)
            print("!!!!!!!!!!!!!!!!!!!!出现异常，重新开始!!!!!!!!!!!!")
            # os.system("python -m uiautomator2 init")
