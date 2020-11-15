package com.yq.jaroutput;

/**
 * Created by yangqiang on 16-11-8.
 */
public class School {

    private String addr;
    private String name;

    public School(String addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
