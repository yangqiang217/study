package com.my.kotlinstudy._8协程

import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

fun excep_launch() {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            println("before withContext exception")
            throw NullPointerException("in global launch")//会导致整个launch内后面的都不走了,会导致app crash
            println("after withContext exception")
        }

        println("before global exception")
        throw NullPointerException("in global launch")
        println("after global exception")
    }
    TimeUnit.SECONDS.sleep(3)
    println("after launch")
}

fun excep_launch_handle() {
    GlobalScope.launch(CoroutineExceptionHandler {_, e ->
        e.printStackTrace()
    }) {
        withContext(Dispatchers.IO) {
            println("before withContext exception")
            throw NullPointerException("in global launch")//会导致整个launch内后面的都不走了,会导致app crash
            println("after withContext exception")
        }

        println("before global exception")
        throw NullPointerException("in global launch")
        println("after global exception")
    }
    TimeUnit.SECONDS.sleep(3)
    println("after launch")
}

fun excep_async() = runBlocking {
    val deferred = GlobalScope.async {
        withContext(Dispatchers.IO) {
            println("before withContext exception")
            throw NullPointerException("in global launch")//会导致整个launch内后面的都不走了,会导致app crash
            println("after withContext exception")
        }

        println("before global exception")
        throw NullPointerException("in global launch")
        println("after global exception")
    }
    try {//只要try住await就不会崩了
        deferred.await()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    TimeUnit.SECONDS.sleep(3)
    println("after launch")
}