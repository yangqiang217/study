package com.example.dragrect;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button btnBegin, btnStop;
    private DragRectView dragRectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dragRectView = (DragRectView) findViewById(R.id.dragRectView);
        dragRectView.setOnTouchUpListener(new DragRectView.OnTouchUpListener() {
            @Override
            public void onTouchUp(int left, int top, int right, int bottom) {
                Toast.makeText(MainActivity.this, "l:" + left + ", t:" + top + ", r:" + right + ", " +
                        "b:" + bottom, Toast.LENGTH_SHORT).show();
            }
        });

        btnBegin = (Button) findViewById(R.id.begin);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragRectView.begin();
            }
        });

        btnStop = (Button) findViewById(R.id.stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragRectView.stop();
            }
        });
    }
}
