package com.my.kotlinstudy._5Lambda

import java.lang.StringBuilder

data class Person1(val name: String, val age: Int)

fun main() {
    //filter
    val list = listOf(1, 2, 3)
    println(list.filter { it % 2 == 0 })

    val list1 = listOf(Person1("yq", 1), Person1("hhx", 2))
    println(list1.filter { it.age >= 30 })


    //map，和rxjava的map类似
    println(list.map { it * it })
    println(list1.map { it.age })//==
    println(list1.map { Person1::age })

    //filter map合用
    println(list1.filter { it.age > 3 }.map { it.age }.map { it.toString() })

    //all any
    println(list1.all { p: Person1 -> p.age > 30 })//bollean，全都满足返回true
    println(list1.any { p: Person1 -> p.age > 30 })//bollean，任意满足返回true，等于
    println(!list1.any { p: Person1 -> p.age > 30 })

    //count
    println(list1.count { p: Person1 -> p.age > 30 })//int，满足的数量
    println(list1.filter {  p: Person1 -> p.age > 30  }.size)//没有count高效，filter需要开辟存储空间

    //find
    println(list1.find {  p: Person1 -> p.age > 30  })//找到第一个满足的元素并返回

    //groupby
    println(list1.groupBy { it.age })//返回一个map，key是age，val是个list，即：Map<Int, List<Person>>
    //按首字母分组
    val list2 = listOf("a", "b", "c")
    println(list2.groupBy(String::first))

    //flatmap flatten，嵌套集合中的元素处理
    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })//[a, b, c, d, e, f]

    //对map操作
    val numbers = mapOf(1 to "one", 2 to "two")
    println(numbers.mapValues { it.value.toUpperCase() })





}

/**
 * 惰性集合操作，序列
 */
fun sequence() {
    val list1 = listOf(Person1("yq", 1), Person1("hhx", 2))
    //上面链式调用的问题: filter和map都会创建临时列表，效率会很低
    val a = list1.map(Person1::name).filter { it.startsWith("a") }
    //提高效率，结果和a一样，这种写法没有创建中间集合
    val a2 = list1.asSequence()
            .map(Person1::name).filter { it.startsWith("a") }
            .toList()//只有调了toList这个末端操作，map和filter这些中间操作才会被调用，类似于rx的订阅

    //单纯map和filter是先在每个元素上map然后filter，而序列是元素挨个进行所有操作，所以下面找到2后3、4就不走了
    println(listOf(1, 2, 3, 4).asSequence().map { it * it }.find { it > 3 })
}


/**
 * with
 * 不用with：调用了result实例上几个不同的方法，且每次调用都重复result这个名称
 */
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("end")
    return result.toString()
}
/**
 * 用with
 * with其实是一个两个参数的函数，这个例子中参数分别是stringBuilder和一个lambda，
 * 利用把lambda放在括号外的约定，所以也可以with(stringBuilder, {})，但可读性差
 */
fun alphabet1(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)//this可以不要
        }
        append("end")
        this.toString()//this可以不要
    }
}

/**
 * 进一步简化
 */
fun alphabet2() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        this.append(letter)//this可以不要
    }
    append("end")
    this.toString()//this可以不要
}


/**
 * apply
 * 和with类似，区别是apply始终返回作为实参传递给它的对象
 * apply是一个扩展函数
 */
fun alphabet3() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        this.append(letter)//this可以不要
    }
    append("end")
    this.toString()//this可以不要
}

/**
 * 定义一个自定属性的TextView
 */
//fun createCustomTextView(context: Context) =
//    TextView(context).apply {
//        text = "shit"
//        textSize = 20.0
//        setPadding(10, 0, 0, 0)
//    }