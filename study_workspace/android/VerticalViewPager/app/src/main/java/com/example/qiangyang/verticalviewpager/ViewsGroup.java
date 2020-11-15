//package com.example.qiangyang.verticalviewpager;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * Created by qiangyang on 2017/4/25.
// */
//@Deprecated
//public class ViewsGroup extends FrameLayout {
//
//    private Button btn;
//    private TextView tv;
//
//    private int id;
//
//    private Bean bean;
//
//    public ViewsGroup(@NonNull Context context, int id) {
//        super(context);
//        this.id = id;
//        init(context);
//    }
//
//    public ViewsGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    private void init(Context context) {
//        LinearLayout container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_item, null);
//        addView(container, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//        btn = (Button) findViewById(R.id.btn1);
//        tv = (TextView) findViewById(R.id.tv1);
//
//        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//    }
//
//    public void setData(Bean bean) {
//        this.bean = bean;
//        btn.setText(bean.btnText);
//        tv.setText(bean.tvText);
//    }
//
//    public void setBtnClickListener(OnClickListener onClickListener) {
//        btn.setOnClickListener(onClickListener);
//    }
//
//    @Override
//    public int getId() {
//        return id;
//    }
//
//}
