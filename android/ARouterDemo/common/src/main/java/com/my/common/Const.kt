package com.my.common

object Const {
    // 配置的path至少需要两级，如/xx/xxx
    const val PATH_SECOND_ACT = "/app/secondActivity"
    const val PATH_THIRD_ACT = "/app/thirdActivity"

    //不同module不能定义一样的组（即第一个/后面的东西）
    const val PATH_IN_MODULE_ACT = "/common/inModuleActivity"

    const val PATH_SINGLE_SERVICE = "/service/single"
    const val PATH_SINGLE2_SERVICE = "/service/single2"
}