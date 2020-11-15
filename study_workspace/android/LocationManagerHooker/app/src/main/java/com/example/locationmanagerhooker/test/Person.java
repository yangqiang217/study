package com.example.locationmanagerhooker.test;

public class Person {
    private String name;
    private Car car;

    public Person(String name, Car car) {
        this.name = name;
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public Car getCar() {
        return car;
    }

    public Person setCar(Car car) {
        this.car = car;
        return this;
    }
}
