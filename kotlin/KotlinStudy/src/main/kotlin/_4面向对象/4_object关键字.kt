/*
object用处：定义一个类并同时创建一个对象实例
场景：
    单例
    伴生对象
    对象表达式用来替代java匿名内部类
 */
package _4面向对象

fun main() {
    //访问方式就像java的静态
    Payroll.allEmployees.add("a")
    Payroll.allEmployees.add("b")
    Payroll.calSalary()

    A.bar()

    Person2.Loader.fromJson("")

    val p = Person3.toJson(Person3("name"))
}

/**
 * 对象声明，单例工资单
 * 和类一样，对象声明可以包含属性、方法、初始化语句块等，唯一不允许的是构造方法（主、从），
 * 因为声明的时候已经创建了，不需要别的地方调用构造函数。
 */
object Payroll {
    val allEmployees = arrayListOf<String>()

    fun calSalary() {
        for (name in allEmployees) {
            println(name)
        }
    }
}
/**
 * 多个person都只有一个NameComparator实例
 */
data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(o1: Person, o2: Person): Int = o1.name.compareTo(o2.name)
    }
}

/**
 * kot中的类不能有静态成员，不能用static，kot依赖包级别函数（大多情况下替代static）和对象生命（其它情况下）
 * 伴生对象可以访问类中所有private成员，包括private构造方法，是实现工厂模式的理想选择
 * 类似于java中的static方法，因为kot没法用static
 */
class A {
    companion object {
        fun bar() {
            println("companion obj called")
        }
    }
}
//使用
/**
 * 定义一个包含两个构造函数的user
 */
class User5 {
    val nickName: String

    constructor(email: String) {
        nickName = email.substringBefore("@")
    }
    constructor(id: Int) {
        nickName = id.toString()
    }
}
//用工厂方法改造，好处有很多，比如可以在new之前判断是否为空而复用，
//但如果User6要用来做父类，那就还是用多个构造函数的形式，因为伴生对象成员不能被重写
class User6 private constructor(val nickName: String){
    companion object {
        fun newEmailUser(email: String) {
            User(email.substringBefore("@"))
        }
        fun newIdUser(id: Int) {
            User(id.toString())
        }
    }
}


/**
 * 作为普通对象使用的伴生对象
 */
class Person2(val name: String) {
    //命名伴生对象(名字完全可以不要)，没名字名字就是Companion，在java中就用这个名字调用
    companion object Loader {
        fun fromJson(json: String) : Person2 {
            return Person2("")
        }
    }
}
/**
 * 伴生对象实现接口
 */
interface JsonFactory<T> {
    fun fromJson(json: String): T
}
class Person3(val name: String) {
    companion object : JsonFactory<Person3> {
        override fun fromJson(json: String): Person3 {
            return Person3("")
        }
    }
}
/**
 * 伴生对象的扩展函数
 */
fun Person3.Companion.toJson(person: Person3): String {
    return person.toString()
}

/**
 * 对象表达式：改变写法的匿名内部类
 */
interface OnClickListener {
    fun onClick()
}
class View2 {
    var onClickListener: OnClickListener
        get() {
            TODO()
        }
        set(value) {}

    fun setClickListener(lis: OnClickListener) {
        onClickListener = lis
    }
}
fun onCreate() {
    val v: View2 = View2()
    var count = 0
    /*
    使用匿名对象实现监听
    和对象生命不同，匿名对象不是单例的。每次对象表达式执行都会创建新实例
    （这种写法后面会用lambda替代）
     */
    v.setClickListener(object : OnClickListener {
        override fun onClick() {
            //可以访问外面变量，且不需要显式指明为final
            count++
        }
    })
}