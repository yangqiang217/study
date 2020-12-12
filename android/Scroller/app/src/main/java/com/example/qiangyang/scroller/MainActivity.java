package com.example.qiangyang.scroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Scroller scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scroller = new Scroller(this);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroller.startScroll(0, 0, 400, 0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        while (scroller.computeScrollOffset()) {
//                            System.out.println(scroller.getCurrX());
//                        }
                    }
                }).start();
            }
        });
    }
}
