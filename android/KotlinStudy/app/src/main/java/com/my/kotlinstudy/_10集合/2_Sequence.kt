package com.my.kotlinstudy._10集合

/**
 * 序列(Sequences) 的秘诀在于它们是共享同一个迭代器(iterator) ---序列允许 map操作 转换一个元素后，
 * 然后立马可以将这个元素传递给 filter操作 ，而不是像集合(lists) 一样等待所有的元素都循环完成了map操作后，
 * 用一个新的集合存储起来，然后又遍历循环从新的集合取出元素完成filter操作。通过减少循环次数
 *
 * 提高对list操作的效率
 */
fun main() {
    //构建
    //1 普通
    val seq = sequenceOf("four", "three", "two", "one")
    //2 从迭代器
    val list = listOf("one", "two", "three", "four")
    val seqFromList = list.asSequence()
    //3 从函数
    val seqFromFun = generateSequence(1) { it + 2}//无限的
    val seqFromFunFinite = generateSequence(1) {//有限的：最后一个元素返回null
        if (it + 2 < 10) it + 2 else null
    }
    println("seqFromFun: ${seqFromFun.take(5).toList()}")
//    println(seqFromFun.count())//error
    println("seqFromFunFinite.count(): ${seqFromFunFinite.count()}")
    //4 从chunk
    val seqFromChunk = sequence {
        yield(1)
        yieldAll(listOf(3, 5))
        yieldAll(generateSequence(7) { it + 2 })
    }
    println("seqFromChunk: ${seqFromChunk.take(10).toList()}")

}