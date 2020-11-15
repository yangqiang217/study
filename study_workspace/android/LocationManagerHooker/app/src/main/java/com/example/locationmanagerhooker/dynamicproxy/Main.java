package com.example.locationmanagerhooker.dynamicproxy;

public class Main {
    public static void main() {
        ProxySaler proxySaler = new ProxySaler();
        Person object = (Person) proxySaler.newInstall(new XiaoQiang("XiaoQiang"));
        object.buy();
        object.buy1();
    }
}
