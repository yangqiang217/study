package _4面向对象

/**
 * 直接写在类后面()里的东西就是主构造方法
 */
class User /*constructor关键字可以省略*/(_nickName: String) {
    val nickName: String
    init {
        nickName = _nickName
    }
}