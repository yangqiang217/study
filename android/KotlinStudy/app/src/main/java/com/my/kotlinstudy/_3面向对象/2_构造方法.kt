package com.my.kotlinstudy._3面向对象

fun main() {

    val U0 = U0()

}

class U0 {
    init {
        println("u0 in init")//没有显式主构造，init还是会调用，因为会生成一个隐式无参构造
    }
}

open class U(name: String) {

    lateinit var lateP: User
//    lateinit var lateInt: Int//lateinit不能是原始类型

    open val u1: Int = 0
    open var u2: Int = 0
    /**
     * 主构造函数不能包含任何的代码。初始化的代码可以放到以 init 关键字作为前缀的初始化块(initializerblocks)中
     */
    init {
    }

    fun a() {
        if (::lateP.isInitialized) {//判断lateinit的变量是否已经初始化

        }
    }
}

/**
 * 需要U()或constructor()
 */
class U2(_name: String) : U(_name) {
    //可以用var覆写val
    override var u1 = 1
    //不能用val覆写var，因为一个 val 属性本质上声明 了一个 get 方法，而将其覆盖为 var 只是在子类中额外声明一个 set 方法。
//    override val u2 = 1
}

/**
 * 直接写在类后面()里的东西就是主构造方法
 * 如果主构造没有注解或者可见性修饰符，关键字constructor可以省略
 */
open class User /*constructor*/(_nickName: String, _age: Int = 0) {
    val nickName = _nickName
    fun a() {
        println(nickName)
    }
}
class User2 (val nickName/*直接加val表示nickname就是成员变量了*/: String) {
    fun a() {
        println(nickName)
    }
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
    constructor(ctx: String, attr: Int): this(ctx)/*委托给单构造函数*/ {
    }
}
class Button2 : View {
    //通过从形式调用父类从构造方法(有一个就行)
    constructor(ctx: String): super(ctx) {
        useA()
    }
    constructor(ctx: String, attr: Int): super(ctx, attr) {
    }
    //或者用this调用自己的别的构造方法，就不用调super而委托给调的那个调super
    constructor(attr: Int): this("1", attr) {

    }

    fun useA() {
        lis.onClick("shit")
    }

    val lis = object : Listener {
        override fun onClick(str: String) {
            println("onClick")
        }
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
    val height: Int = 1// 不能有初始化*/
//        set(value) {//val 不能有set
//            println("")
//        }
        get() {
            println()
            return field
        }
}

interface Listener {
    fun onClick(str: String)
}



/**
 * init和constructor执行顺序
 * 对象里面init统一早于constructor，内部顺序按代码顺序
 * companion里面的init早于所有对象的，但如果companion的init之前new了这个对象，那么就会先走对象的
 */
class TestObj {

    init {
        println("TestObj init1")
    }

    constructor() {
        println("TestObj constructor1")
    }

    constructor(a: Int) {
        println("TestObj constructor1")
    }


    init {
        println("TestObj init2")
    }

    fun print() {
    }

    companion object {
        val a = TestObj()

        init {
            println("TestObj init in companion")
        }
    }
}