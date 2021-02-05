package com.example.popupwindow;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mRoot;
    private TextView mTvAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = findViewById(R.id.root);
        mTvAnchor = findViewById(R.id.tv);

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                show();
            }
        });
        findViewById(R.id.root).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                show();
//                mTvAnchor.setselect
//                mTvAnchor.setX(200);
                return true;
            }
        });
    }

    private void show() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pop, null, false);

        TextView tv = view.findViewById(R.id.tv_pop);

        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

//        popupWindow.showAsDropDown(mTvAnchor, 0, 0, Gravity.LEFT);//window和anchor左边对齐
//        popupWindow.showAsDropDown(mTvAnchor, 0, 0, Gravity.TOP);//和left一样
//        popupWindow.showAsDropDown(mTvAnchor, 0, 0, Gravity.RIGHT);//window左边和anchor右边对齐
//        popupWindow.showAsDropDown(mTvAnchor, 100, 0, Gravity.CENTER_HORIZONTAL);
        popupWindow.showAsDropDown(mTvAnchor, 0, 0, Gravity.BOTTOM | Gravity.LEFT);//和left一样

//        popupWindow.showAtLocation(mRoot, Gravity.TOP, 0, 400);

//        tv.setX(100);
    }
}