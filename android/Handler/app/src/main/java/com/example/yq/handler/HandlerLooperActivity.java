package com.example.yq.handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

/**
 * 一些handler和looper、msgQueue的东西
 */
public class HandlerLooperActivity extends Activity {

    MyHandler handler;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handlerlooper);

        Looper.prepare();
        handler = new MyHandler();

        btn = (Button) findViewById(R.id.btn2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run () {
                        Message msg = Message.obtain();//跟new什么区别。缓存了大小为50的msg用来复用的，避免new对象
                        msg.what = 1;
                        /**
                         * Handler里有个变量messageQueue，而这个messageQueue是Handler里另一个变量mLooper的变量。说明一个Handler对应一个msgQueue对应一个Looper
                         * 那么问题来了Handler里的Looper变量怎么来的：mLooper = Looper.myLooper() = Looper.sThreadLocal.get();（为什不直接mLooper = new Looper()而用ThreadLocal？会new太多Looper，加大负载）
                         *
                         * sendMessage完以后最终调用msgQueue的enqueueMessage(Message msg, long when)，加入到msgQueue的链表中，等待Looper调loop来循环处理
                         * TODO 字线程sendMessage完是怎么把东西搞到主线程的？因为到loop()方法的时候已经是在主线程了 ans:又不是进程间通信，直接加个锁俩线程直接可以访问了
                         */
                        handler.sendMessage(msg);

                        /**
                         * 另一种：两种什么区别
                         * ans：其实这里最终调的也是sendMsg，但是new runnable给msg设置了个callback，在处理完dispatchmsg的时候如果发现msg的callback不为空，就走这个callback不走handleMsg，否则，走handleMsg
                         */
                        handler.post(new Runnable() {
                            @Override
                            public void run () {
                                System.out.println(1);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println(1);
                    break;
                case 2:
                    System.out.println(2);
                    break;
            }
        }
    }
}
