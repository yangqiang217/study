package com.my.arouterdemo

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        INS = this

        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }

    companion object {
        lateinit var INS: MyApp
    }
}