package com.yq.imageviewer.event;

/**
 * Created by yangqiang on 09/02/2018.
 */

public class LoadFinishEvent {
    int count;

    public LoadFinishEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
