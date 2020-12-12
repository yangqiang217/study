package com.yq.kotlindemo

import android.content.Context
import android.widget.Toast

/**
 * 扩展函数，类似于扩展自Context的函数
 */
fun Context.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}