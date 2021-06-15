package com.my.kotlinstudy._4Lambda和高阶函数

fun main() {
    lookForAlice2(people)
}

val people = listOf(Person("Alice", 29), Person("Bob", 31))

/**
 * 遍历寻找然后返回
 */
fun lookForAlice(people: List<Person>) {
    for (person in people) {
        if (person.name == "Alice") {
            println("Found")
            return
        }
    }

    //or 从lambda返回
    people.forEach {
        if (it.name == "Alice") {
            println("found")
            return//非局部返回
        }
    }
    println("not found")
}

/**
 * 局部返回
 */
fun lookForAlice1(people: List<Person>) {
    people.forEach label@ {
        if (it.name == "Alice") {
            println("found")
            /*
            局部返回，和java for循环的break类似，
            终止lambda，继续后面代码
            所以end也会打印
            return和@中间不能有空格
             */
            return@label
        }
    }
    println("end")
}

/**
 * 匿名函数，默认使用局部返回
 */
fun lookForAlice2(people: List<Person>) {
    people.forEach(fun(person) {
        if (person.name == "Alice") {
            println("found")
            return
        }
        //会被执行
        println("${person.name} not alice")
    })
}
