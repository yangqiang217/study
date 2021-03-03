package com.my.kotlinstudy.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay

class MainViewModel: BaseViewModel() {

    fun run(): MutableLiveData<Int> {
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()
        launch {
            delay(1000)
            Log.d("yqtest", "in launch thread: ${Thread.currentThread().name}")
            liveData.value = 11
        }
        Log.d("yqtest", "after launch thread: ${Thread.currentThread().name}")
        return liveData
    }
}