package com.example.jetpackdemo.bean;

public class Movie {

    private int id;
    private String name;
    private float price;

    public Movie(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
