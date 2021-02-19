package com.my.kotlinstudy._10协程

import kotlinx.coroutines.*

fun main() {
//    create()
    singleCoroutine()
}

fun create() {
    println("协程初始化开始，时间: " + System.currentTimeMillis())

    GlobalScope.launch(Dispatchers.Unconfined) {
        println("协程初始化完成，时间: " + System.currentTimeMillis())
        for (i in 1..3) {
            println("协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
        }
        delay(500)
        for (i in 1..3) {
            println("协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
        }
    }

    println("主线程 sleep ，时间: " + System.currentTimeMillis())
    Thread.sleep(1000)
    println("主线程运行，时间: " + System.currentTimeMillis())

    for (i in 1..3) {
        println("主线程打印第$i 次，时间: " + System.currentTimeMillis())
    }

}

fun singleCoroutine() {
    GlobalScope.launch(Dispatchers.Main) {
        println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
        val token = getToken()
        val response = getResponse(token)
        setText(response)
    }
}

fun multiCoroutine() {
    GlobalScope.launch(Dispatchers.Unconfined) {
        var token = GlobalScope.async(Dispatchers.Unconfined) {
            return@async getToken()
        }.await()

        var response = GlobalScope.async(Dispatchers.Unconfined) {
            return@async getResponse(token)
        }.await()

        setText(response)
    }
}

/**
 * suspend 修饰的方法挂起的是协程本身，而非该方法
 */
suspend fun getToken(): String {
    delay(3000)
    println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
    return "ask"
}

suspend fun getResponse(token: String): String {
    delay(100)
    println("getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
    return "response"
}

fun setText(response: String) {
    println("setText 执行，时间:  ${System.currentTimeMillis()}")
}