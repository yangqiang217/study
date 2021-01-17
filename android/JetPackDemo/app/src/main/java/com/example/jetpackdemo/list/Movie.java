package com.example.jetpackdemo.list;

public class Movie {

    private String name;
    private float price;

    public Movie(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Movie setName(String name) {
        this.name = name;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public Movie setPrice(float price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
