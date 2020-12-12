package com.example.locationmanagerhooker.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxySaler implements InvocationHandler {

    public Person mPerson;

    public Object newInstall(Person person) {
        this.mPerson = person;
        return Proxy.newProxyInstance(person.getClass().getClassLoader(), person.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invoke");
        if (method.getName().equals("buy")) {
//            mPerson.buy();
            System.out.println("fake buy");
        }
        if (method.getName().equals("buy1")) {
            mPerson.buy1();
        }
        System.out.println("after invoke");
        return null;
    }
}
