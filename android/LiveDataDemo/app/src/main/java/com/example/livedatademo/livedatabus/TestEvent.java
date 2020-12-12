package com.example.livedatademo.livedatabus;

import android.support.annotation.NonNull;

public class TestEvent {
    private String content;

    public TestEvent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "test event, content: " + content;
    }
}
