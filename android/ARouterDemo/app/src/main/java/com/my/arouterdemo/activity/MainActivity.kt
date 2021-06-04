package com.my.arouterdemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.my.arouterdemo.R
import com.my.common.Const
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_same_module_act.setOnClickListener(this)
        btn_in_module_act.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult, requestCode: $requestCode, resultCode: $resultCode")
    }

    override fun onClick(v: View?) {
        when (v) {
            //页面跳转，只需要在目标act上面加@Router注解
            btn_same_module_act -> {
                ARouter.getInstance()
                    .build(Const.PATH_SECOND_ACT)
                    //传递参数
                    .withString(SecondActivity.KEY_STR, "shit")
//                    .navigation()
                    //类似startActivityForResult
                    .navigation(this, REQUEST_CODE_SECOND)
            }
            //不同模块的act跳转和同模块的一样
            btn_in_module_act -> {
                ARouter.getInstance()
                    .build(Const.PATH_IN_MODULE_ACT)
                    .navigation()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_SECOND = 100
    }
}