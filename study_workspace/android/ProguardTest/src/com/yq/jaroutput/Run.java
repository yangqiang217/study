package com.yq.jaroutput;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangqiang on 16-9-13.
 */
public class Run extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getId() {
        return 100 + super.getId();
    }
}
