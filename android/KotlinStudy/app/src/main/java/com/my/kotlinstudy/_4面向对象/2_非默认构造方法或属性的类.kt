package com.my.kotlinstudy._4面向对象

fun main() {
    val u = User("name")
    println(u.nickName)

    val u2 = User2("name")
    println(u2.nickName)

    val u4 = User4("name4")
    u4.address = "add"
    println(u4.address)
}
/**
 * 直接写在类后面()里的东西就是主构造方法
 * 如果主构造没有注解或者可见性修饰符，关键字constructor可以省略
 */
open class User /*constructor*/(_nickName: String, _age: Int = 0) {
    val nickName = _nickName
}
class User2 (val nickName/*直接加val表示nickname就是成员变量了*/: String) {
}

/**
 * 如果有父类，主构造方法同样需要初始化父类。
 */
class TwitterUser : User("name") {

}

/**
 * private的构造方法
 */
class Secretive private constructor() {
}

/**
 * 从构造方法
 * 不要声明多个从构造方法来重载和提供参数的默认值，应该直接标明默认值
 */
open class View {
    constructor(ctx: String) {
    }
    constructor(ctx: String, attr: Int) {
    }
}
class Button2 : View {
    //通过从形式调用父类从构造方法(有一个就行)
    constructor(ctx: String): super(ctx) {
    }
    constructor(ctx: String, attr: Int): super(ctx, attr) {
    }
    //或者用this调用自己的别的构造方法，就不用调super而委托给调的那个调super
    constructor(attr: Int): this("1", attr) {

    }
}
//通过主形式调用父构造方法
class Button3(ctx: String) : View(ctx) {

}

/**
 * 接口包含抽象属性声明
 */
interface User3 {
    val nickName: String
    val age: Int
        get() = nickName.length//有get子类就不用初始化了
}
class PrivateUser(override val nickName: String) : User3 {

}
class SubscribingUser(val email: String) : User3 {
    override val nickName: String
        get() = email.substringBefore("@")//自定义getter
}
class FacebookUser(val accountId: Int): User3 {
    override val nickName: String = getFacebookName(accountId)

    private fun getFacebookName(accountId: Int): String {
        return ""
    }
}

/**
 * getter setter
 */
class User4(val name: String) {
    var address: String = "null"
        set(value) {//u4.address = "add"会调用set
            println("add changed")
            field = value
        }
        get() {
            println("get called")
            return "shit"
        }
    var age: Int = 0
        private set(value) {//不能再外面修改此属性，编译报错
            field = value
        }
        /*private getter不能是private的，会报错 */get() {
            return 1
        }
}