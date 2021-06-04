package com.my.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.my.common.iservice.ISingleService
import kotlinx.android.synthetic.main.activity_in_module.*

@Route(path = Const.PATH_IN_MODULE_ACT)
class InModuleActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_module)

        btn_call_service.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_call_service -> {
                /*
                假如想在common里拿app里的数据，common没有依赖app，所以不能直接拿，可以约定这种service
                在app里写一个ISingleService的实现类，并添加@Router注解就行

                但是有多个ISingleService的实现呢？
                同一个path的时候，只有一个调用
                不同path的时候，也只有一个调用
                 */
                val res = ARouter.getInstance().navigation(ISingleService::class.java).getData()
                Log.i(TAG, "single service get data: $res")
            }
        }
    }

    companion object {
        const val TAG = "InModuleActivity"
    }
}