package com.my.kotlinstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal typealias LaunchBlock = suspend CoroutineScope.() -> Unit

open class BaseViewModel: ViewModel() {

    fun launch(block: LaunchBlock) {
        viewModelScope.launch {
            block()
        }
    }
}