package com.yq.imageviewer.event;

/**
 * Created by yangqiang on 30/03/2018.
 */

public class MainBottomAnimEvent {
    private boolean hide;

    public boolean isHide() {
        return hide;
    }

    public MainBottomAnimEvent setHide(boolean hide) {
        this.hide = hide;
        return this;
    }
}
