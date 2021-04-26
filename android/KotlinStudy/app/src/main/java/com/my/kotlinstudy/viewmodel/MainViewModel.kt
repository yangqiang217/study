package com.my.kotlinstudy.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainViewModel: BaseViewModel() {

    fun request(): MutableLiveData<Int> {
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()
        launch {
//            delay(3000)
            var a = 1
            while (true) {
                a = 2
            }
            Log.d("yqtest", "in launch thread: ${Thread.currentThread().name}")//main
            liveData.value = 11
        }
        Log.d("yqtest", "after launch thread: ${Thread.currentThread().name}")//main
        return liveData
    }

    fun request2() {
        GlobalScope.launch {

        }
            launch {
                withContext(Dispatchers.IO) {
                    Log.d("yqtest", "in request2 loc1 thread: ${Thread.currentThread().name}")//main
                    TimeUnit.SECONDS.sleep(3)
                    var s: String? = null
                    println(s!!.substring(1, 2))
                }
                Log.d("yqtest", "in request2 loc2 thread: ${Thread.currentThread().name}")//main
            }
        }
}