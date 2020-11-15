package com.example.locationmanagerhooker.test;

import androidx.annotation.NonNull;

public class Car {
    private String brand;

    public Car(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public Car setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return brand;
    }
}
