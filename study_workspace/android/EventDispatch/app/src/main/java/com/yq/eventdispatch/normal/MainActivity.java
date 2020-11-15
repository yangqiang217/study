package com.yq.eventdispatch.normal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yq.eventdispatch.listview.ListViewActivity;
import com.yq.eventdispatch.R;
import com.yq.eventdispatch.Utils;
import com.yq.eventdispatch.listview.MyListView;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private MyButton btn;
    private MyTextView tv;
    private MyLinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (MyButton) findViewById(R.id.btn1);
        ll = (MyLinearLayout) findViewById(R.id.ll1);
        tv = (MyTextView) findViewById(R.id.tv111);

//        ll.requestDisallowInterceptTouchEvent(true);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
//                startActivity(intent);
//            }
//        });

//        findViewById(R.id.btn1).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                boolean pass = false;
//                printLog("MyButton onTouch()  event: " + Utils.getActionName(event.getAction()) + ",  " + pass);
//                return pass;
//            }
//        });

//        ll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                boolean pass = false;
//                printLog("MyLinearLayout onTouch()  event: " + Utils.getActionName(event.getAction()) + ",  " + pass);
//                return pass;
//            }
//        });
    }

    private void printLog(String content) {
        Log.d(TAG, content);
    }
}
