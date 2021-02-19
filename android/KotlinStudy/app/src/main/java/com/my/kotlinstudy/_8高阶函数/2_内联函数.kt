package com.my.kotlinstudy._8高阶函数

/**
 * 每调用一次lambda表达式，一个额外的类就会被创建。且如果lambda捕捉了某个变量，
 * 每次调用的时候都会去创建一个新的对象。这会带来运行时额外开销，导致使用lambda
 * 比使用一个直接执行相同代码的函数效率更低
 *
 * inline函数在使用时编译器不会生成函数调用的代码，而是使用函数实现的真实代码替
 * 换每一次函数调用
 *
 * 使用inline只能提高带有lambda参数的函数的性能，其它的情况需要额外度量和研究。
 * 对于普通函数调用，jvm已经提供了强大的内联支持。在字节码转换成机器码时自动完成。
 *
 * inline函数要短小
 */
fun main() {
    val l = Lock()
    syncronized(l) {
        println("action")
    }
}

inline fun <T> syncronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

class Lock {
    fun lock() {}
    fun unlock() {}
}