package com.my.kotlinstudy._3面向对象

import kotlin.reflect.KProperty

fun main() {
//    val client2 = Client2("name", 1)
//    println(client2)


//    val b = BaseImpl(10)
//    Derived(b).print2()

//    val byLazy = ByLazy()
//    byLazy.print()

//    val e = Example()
//    e.p = "ppp"
//    println(e.p)

    val user = UserBy(mapOf("name" to "John", "age" to 23))
    println(user.age)
}

/**
 * 委托 by
 * 常规委托实现：挨个把动作给内部变量
 */
class DelegatingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()

    override val size: Int
        get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}
/** 用by实现简写的委托 */
class DelegatingCollection2<T>(innerList: Collection<T> = ArrayList<T>()) : Collection<T> by innerList {
}
/**
 * 使用委托实例
 * 只实现add和addAll，其它的比如contains、isEmpty等不用写也不会报错，因为委托了
 */
class CountingSet<T>(val innerSet: MutableCollection<T> = HashSet<T>()) : MutableCollection<T> by innerSet {
    var objectAdded = 0
    override fun add(element: T): Boolean {
        objectAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectAdded += elements.size
        return innerSet.addAll(elements)
    }
}
/**
 * 使用委托实例2
 * Derived 不用实现print方法
 */
interface Base {
    fun print1()
    fun print2()
}
class BaseImpl(val x: Int): Base {
    override fun print1() {
        println("$x shit1")
    }
    override fun print2() {
        println("$x shit2")
    }
}
class Derived(b: Base): Base by b {
    //不想委托的
    override fun print2() {
        println("shitt")
    }
}


/**
 * by lazy
 * by lazy 与 lateinit 不同的是 在使用lateinit 定义的变量前 一定会给他一个实例 保证他不会是空对象，
 * 而 by lazy 则是在第一次使用时 初始化对象 那我们来验证下 什么叫 第一次使用时 初始化对象
 * 多线程也走一次，所以线程安全
 *
 * 和直接初始化的区别是，不用它就不会被初始化
 */
class ByLazy {
    val name: String by lazy {
        println("in by lazy")
        "name"
    }
    fun print() {
        println(name)
        println(name)
    }
}

/**
 * 属性委托
 */
class Example {
    var p: String by Delegate()
}
class Delegate {
    private var _p: String = ""
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return _p
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        _p = value
    }
}

/**
 * map中存储变量
 */
class UserBy(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}