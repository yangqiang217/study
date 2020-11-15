package com.yq.jaroutput;

import okio.Timeout;

/**
 * Created by yangqiang on 16-11-8.
 */
public class UseOutJar {

    public void go() {
        Timeout timeout = new Timeout();
        timeout.clearDeadline();
    }
}
