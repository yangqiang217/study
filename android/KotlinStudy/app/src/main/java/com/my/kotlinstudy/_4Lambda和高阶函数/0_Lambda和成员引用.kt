package com.my.kotlinstudy._4Lambda和高阶函数

fun main() {
    val list: List<String> = listOf("a", "b")
    scope(list)
}

data class Person(val name: String, val age: Int) {
    fun run() {

    }
}

/**
 * 查找年龄最大的常规写法
 */
fun findOldest(list: List<Person>) {
    var max = 0
    var oldest: Person? = null
    for (person in list) {
        if (person.age > max) {
            max = person.age
            oldest = person
        }
    }
    println(oldest)
}
/**
 * 使用kot集合库函数
 */
fun findOldest2(list: List<Person>) {
    //原始非简写lambda
    println(list.maxBy({ p: Person -> p.age }))
    //改造1：如果lambda是函数调用的最后一个实参，可以放到括号外面
    println(list.maxBy() { p: Person -> p.age })
    //改造2：当lambda是函数唯一实参时，可以去掉()
    println(list.maxBy { p: Person -> p.age })
    //改造3：如果lambda参数类型可以被推导，就不需要显式指定
    println(list.maxBy { p -> p.age })
    //终极改造：只用it（it不要滥用，有lambda嵌套的时候不要用，不然不知道是哪个的）
    println(list.maxBy { it.age })
    //如果lambda刚好是函数或者属性的委托，可以用成员引用替换
    println(list.maxBy(Person::age))
}

/**
 * 语法
 */
fun grammar() {
    //完整写法
    val sum: (Int, Int) -> Int = {x: Int, y: Int -> x + y}
    //箭头把实参列表和函数体分隔开
    val sum1 = {x: Int, y: Int -> x + y}
    println(sum(1, 2))

    val people = listOf(Person("yq", 1), Person("hhx", 2))
    val names = people.joinToString(separator = " ", transform = { p: Person -> p.name} )
    println(names)
    //简写
    val names2 = people.joinToString(" ") { p: Person -> p.name}

    //多语句
    val sum2 = { x: Int, y: Int ->
        println("x:$x, y:$y")
        x + y
    }
}

/**
 * 作用域
 */
fun scope(list: List<String>) {
    var count = 0
    //先执行完这个lambda才走后面
    list.forEach {
        println(it)
        //可以访问外面非final变量，实际也是做了一层封装
        count++
    }
    println("end")
}

/**
 * 成员引用
 * 把函数转换成一个值，用来传递它，用::
 */
fun fieldRef() {
    //成员引用，创建一个调用单个方法或者访问单个属性的函数值
    val age = Person::age
    val run = Person::run

    //委托给一个接受多个参数的函数
    val action = { person: Person, message: String -> sendEmail(person, message) }
    val action2 = ::sendEmail

    //构造方法引用存储或延期执行初始化
    val createPerson = ::Person
    val person = createPerson("yq", 1)
}
fun sendEmail(person: Person, message: String) {

}