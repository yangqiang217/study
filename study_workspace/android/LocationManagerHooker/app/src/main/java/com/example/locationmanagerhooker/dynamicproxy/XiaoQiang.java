package com.example.locationmanagerhooker.dynamicproxy;

public class XiaoQiang implements Person {

    private String name;

    public XiaoQiang(String name) {
        this.name = name;
    }

    @Override
    public void buy() {
        System.out.println("xiao qiang buy");
    }

    @Override
    public void buy1() {
        System.out.println("xiao qiang buy1");
    }
}
