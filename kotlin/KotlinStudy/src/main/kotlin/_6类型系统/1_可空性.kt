package _6类型系统

import java.lang.NullPointerException

fun main() {
//    println(strLen(null))
//    println(strLen2(null))

//    elvis("null")

//    az()

//    ignoreNulls(null)

//    lets()

    val init2 = Init2()
    init2.use()
}

/**
 * s一定不为null，否则编译不过
 * 即没有问号的类型不能存null
 */
fun strLen(s: String) = s.length
fun strLen2(s: String?): Int? {
    //可以?连续调用
    s?.substring(1)?.substring(2)?.substring(3)

    println("before")
    val a = s?.length
    println("after, a: $a")
    return a
}

/**
 * Elvis运算符
 */
fun elvis(s: String?) {
    val t: String = s ?: ""//类似于java的String t = !TextUtils.isEmpty(s) ? s : ""
    println(t)

    val t1 = s?.substring(1)?.substring(2) ?: throw NullPointerException("null")
}
fun strLen3(s: String?): Int = s?.length ?: 0


/**
 * 安全转换 as?
 */
fun az(){
    val i: Any? = null
//    val num: Number = i as Number//crash
    val num2: Number = i as? Number ?: 0
    println(num2)
}


/**
 * 非空断言：!!
 * !!的崩溃堆栈只能指示到行而不是说谁空，所以不要这样写：person.company!!.address!!.country
 */
fun ignoreNulls(s: String?) {
    val notNull: String = s!!//非空就返回s，空就抛NullPointer异常（是在这里抛不是下一行）
    println(notNull.length)
}


/**
 * let
 * 要将一个可空值作为 实参传递给一个只接收非空值的函数时
 */
fun lets() {
    val email: String? = "shit"
//    sendEmail(email)//不能编译
//    if (email != null) {//这样才行
//        sendEmail(email)
//    }//或者：
    email?.let {//没有?也可能会崩
//        e -> sendEmail(e)//or
        sendEmail(it)
    }
}
fun sendEmail(email: String){
    println("send email to $email")
}


/**
 * 延迟初始化
 */
class MyService {
    fun perform(): String = "foo"
}
//蛋疼版
class Init1 {
    private var service: MyService? = null

    fun setup() {
        service = MyService()
    }
    fun use() {
        service?.perform()//每次用都要?，但是如果想再setup中延迟初始化，声明的时候又必须带?
    }
}
//
class Init2 {
    //必须var，可以不用赋值
    private lateinit var service: MyService

    fun setup() {
        service = MyService()
    }
    fun use() {
        //如果不走setup直接走use，也会崩：UninitializedPropertyAccessException
        service.perform()
    }
}

/**
 * 可空类型的扩展
 */
fun extend() {
    val s: String? = null
    //可以不用?来访问，加?也行
    s.isNullOrBlank()
}
fun String?.isNullOrBlank(): Boolean = this == null || this.isBlank()

/**
 * 类型参数可空性
 * todo 不用?也行？？？
 */
fun <T> printHashCode(t: T) {
    println(t.hashCode())
    println(t?.hashCode())
}

/**
 * 可空性和java
 * java的@Nullable String会被当做String?，@NotNull String就直接是String
 * 没有注解的就自行判断了，不加?就准备好接收异常，否则就加上?
 */