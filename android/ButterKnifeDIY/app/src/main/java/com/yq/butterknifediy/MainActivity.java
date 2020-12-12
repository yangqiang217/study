package com.yq.butterknifediy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yq.lib_compiler.annotation.BindView;
import com.yq.lib_util.InjectHelper;

public class MainActivity extends Activity {

    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InjectHelper.inject(this);

        tv.setText("i am tv");
    }

}