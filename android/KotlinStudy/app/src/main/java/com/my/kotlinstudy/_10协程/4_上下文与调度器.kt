package com.my.kotlinstudy._10协程

import kotlinx.coroutines.*

fun main41() = runBlocking {
    launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
    println()
}

/**
 * 在不同线程间跳转
 * 使用 withContext 函数来改变协程的上下文，!!而仍然驻留在相同的协程中!!
 */
fun main42() = runBlocking {
    newSingleThreadContext("ctx1").use { ctx1 ->
        newSingleThreadContext("ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                println("start in ctx1, thread: ${Thread.currentThread().name}")//thread: ctx1
                withContext(ctx2) {
                    println("working in ctx2, thread: ${Thread.currentThread().name}")//thread: ctx1
                }
                println("back to ctx1, thread: ${Thread.currentThread().name}")//thread: ctx1
            }
        }
    }
}

/**
 * 子协程
 * 当一个协程被其它协程在 CoroutineScope 中启动的时候， 它将通过 CoroutineScope.coroutineContext 来承袭上下文，
 * 并且这个新协程的 Job 将会成为父协程作业的 子 作业。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。

 * 然而，当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业。 因此它与这个启动的作用域无关且独立运作。
 */
fun main() = runBlocking {
    // 启动一个协程来处理某种传入请求（request）
    val request = launch {
        // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
        GlobalScope.launch {
            println("global start")
            delay(1000)
            println("global end")
        }
        // 另一个则承袭了父协程的上下文
        launch {
            delay(100)
            println("son start")
            delay(1000)
            println("son end")//不会走到这
        }
    }
    delay(500)
    request.cancel() // 取消请求（request）的执行
    delay(1000) // 延迟一秒钟来看看发生了什么
    println("main: Who has survived request cancellation?")
}