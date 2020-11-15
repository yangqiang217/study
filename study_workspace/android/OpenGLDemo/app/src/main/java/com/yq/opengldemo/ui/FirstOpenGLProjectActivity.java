package com.yq.opengldemo.ui;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yq.opengldemo.R;
import com.yq.opengldemo.util.L;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FirstOpenGLProjectActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_screen);

        mGLSurfaceView = findViewById(R.id.glsv);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private static class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            L.e("onSurfaceCreated");
            //设置清空屏幕用的颜色,rgba
            GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.9f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            L.e("onSurfaceChanged");
            //opengl可以渲染的surface的大小
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            L.e("onDrawFrame");
            //擦除屏幕所有颜色，并用glClearColor设置的颜色填充屏幕
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }
    }
}
