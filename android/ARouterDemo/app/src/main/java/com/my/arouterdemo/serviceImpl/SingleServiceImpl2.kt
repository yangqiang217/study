package com.my.arouterdemo.serviceImpl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.my.common.Const
import com.my.common.iservice.ISingleService

@Route(path = Const.PATH_SINGLE2_SERVICE)
class SingleServiceImpl2 : ISingleService {
    override fun getData(): String {
        return "got data in 2"
    }

    override fun init(context: Context?) {
    }
}