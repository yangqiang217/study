package com.my.arouterdemo.serviceImpl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.my.common.Const
import com.my.common.iservice.ISingleService

@Route(path = Const.PATH_SINGLE_SERVICE)
class SingleServiceImpl : ISingleService {
    override fun getData(): String {
        return "got dat"
    }

    override fun init(context: Context?) {
    }
}