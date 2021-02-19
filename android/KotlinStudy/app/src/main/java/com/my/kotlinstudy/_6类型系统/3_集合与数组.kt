package com.my.kotlinstudy._6类型系统

fun main() {
    println(readNumbers().size)
}

/**
 * 元素可空的List
 */
fun readNumbers(): List<Int?> {
    val res = ArrayList<Int?>()
    res.add(1)
    res.add(2)
    res.add(null)
    res.add(null)
    println(res.size)
    //过滤掉null
    return res.filterNotNull()
}

/**
 * 自己和元素都可空的List
 */
fun emptyList(list: List<Int?>?) {
    println(list?.size)
}

/**
 * 只读集合和可变集合
 * 每一种java集合在kot中都有两种表示，一种只读，一种可变
 *
 * 类型       只读      可变
 * list      listOf     mutableListOf/arrayListOf
 * set       setOf      mutableSetOf/hashSetOf/linkedSetOf/sortedSetOf
 * map       mapOf      mutableMapOf/hashMapOf/linkedMapOf/sortedMapOf
 */
fun <T> copy(source: List<T>, target: MutableList<T>) {
    for (item in source)
        target.add(item)

//    for (item in target)
//        source.add(item)//根本就没有add方法
}