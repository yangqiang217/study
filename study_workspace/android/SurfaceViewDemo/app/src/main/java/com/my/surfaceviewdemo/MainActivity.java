package com.my.surfaceviewdemo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private SurfaceView sv;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint paint;

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        ll = (LinearLayout) findViewById(R.id.ll);

//        sv = (SurfaceView) findViewById(R.id.sv);
//        mSurfaceHolder = sv.getHolder();
//        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                System.out.println("============surfaceCreated========");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        draw();
//                    }
//                }).start();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("surface view parent: " + sv.getParent());
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sv.getLayoutParams();
//                lp.width = 500;
//                lp.height = 500;
//                sv.setLayoutParams(lp);

                ll.scrollBy(300, 300);
//                sv.scrollBy(300, 300);

//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(sv, "scaleX", 1, 0.4f);
//                objectAnimator.setDuration(2000);
//                objectAnimator.start();
            }
        });
    }

    private void draw() {
        try {
            System.out.println("============draw========");
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawCircle(500,500,300,paint);
            mCanvas.drawCircle(100,100,20,paint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}