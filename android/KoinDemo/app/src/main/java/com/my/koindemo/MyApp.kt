package com.my.koindemo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            //androidLogger 可以将 Koin 日志输出从默认的 java logger 框架切换到 Android Logcat，更加符合习惯，同时也可以自定义日志级别。
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            androidFileProperties()
            modules(listOf(myModule, viewModelModule))
        }
    }
}