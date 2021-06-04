package com.my.arouterdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.my.arouterdemo.R
import com.my.common.Const
import kotlinx.android.synthetic.main.activity_second.*

@Route(path = Const.PATH_SECOND_ACT)
class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val param = intent.getStringExtra(KEY_STR)

        Log.i(TAG, "onCreate, param: $param")

        btn_second.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {
            btn_second -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    companion object {
        const val TAG = "SecondActivity"
        const val KEY_STR = "key_str"
    }

}