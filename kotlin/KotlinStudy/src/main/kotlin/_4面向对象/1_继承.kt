package _4面向对象

fun main() {

}

//接口
interface Clickable {
    //接口中的成员始终是open的，不能标记为final
    fun click()
    fun showOff() = println("show off")
}
interface Focusable {
    fun showOff() = println("show off")
}
class Button : Clickable, Focusable {
    //override必须写
    override fun click() = println("clicked")

    override fun showOff() {
        //在接口有默认实现的前提下，super必须显式指定
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}

//open final abstract
class RichButton : Clickable {

    //默认final，子类不能重写
    fun disable() {}
    //子类能重写
    open fun animate() {}
    //重写的open函数默认是open
    /*final 重写的open加final禁止重写*/override fun click() {
        TODO("Not yet implemented")
    }

}
abstract class Animated {
    //默认open的
    abstract fun animate()

    open fun stopAnimating(){}

    fun animateTwice(){}
}

/**
 * 内部类
 */
class TextView{
    val text = "1"

    /**
     * 嵌套类，不加任何东西就和java的加static一样
     */
    class State {
        fun getText() {
//            val a: String = text error，无法访问外部类的text
        }
    }

    /**
     * 内部类，加inner就和java的不加任何东西一样
     */
    inner class State2 {
        fun getText() {
            val a: String = text
        }
        fun getOuter(): TextView = this@TextView
    }
}
