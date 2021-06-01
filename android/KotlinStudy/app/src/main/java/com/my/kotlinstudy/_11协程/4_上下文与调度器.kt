package com.my.kotlinstudy._11协程

import kotlinx.coroutines.*

fun main41() = runBlocking {
    /*
    Unconfined             : I'm working in thread main
    Default                : I'm working in thread DefaultDispatcher-worker-1
    main runBlocking       : I'm working in thread main
    io                     : I'm working in thread DefaultDispatcher-worker-2
    withContext io         : I'm working in thread DefaultDispatcher-worker-1
    ---end in thread main
    newSingleThreadContext: I'm working in thread MyOwnThread
     */

    launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking       : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // 在调用的线程直接执行。
        println("Unconfined             : I'm working in thread ${Thread.currentThread().name}")
    }
    /*
    当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default 代表的默认调度器。
    默认调度器使用共享的后台线程池。
    所以 launch(Dispatchers.Default) { …… } 与 GlobalScope.launch { …… } 使用相同的调度器。
    这个调度器经过优化，可以在主线程之外执行 cpu 密集型的工作。例如对列表进行排序和解析 JSON。在线程池中执行。
     */
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default                : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) {
        delay(3000L)
        println("io                     : I'm working in thread ${Thread.currentThread().name}")
    }
    withContext(Dispatchers.IO) {
        delay(3000L)
        println("withContext io         : I'm working in thread ${Thread.currentThread().name}")
    }
    /*
    将使它获得一个新的线程,一个专用的线程是一种非常昂贵的资源。 在真实的应用程序中两者都必须被释放，
    当不再需要的时候，使用 close 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。
     */
    launch(newSingleThreadContext("MyOwnThread")) {
        delay(3000L)
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }

    println("---end in thread ${Thread.currentThread().name}")
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
fun main43() = runBlocking {
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