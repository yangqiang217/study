package com.my.common.iservice

import com.alibaba.android.arouter.facade.template.IProvider

interface ISingleService : IProvider {
    fun getData(): String
}