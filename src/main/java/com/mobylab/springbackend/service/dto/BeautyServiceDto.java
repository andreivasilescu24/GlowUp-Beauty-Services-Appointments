package com.mobylab.springbackend.service.dto;

public class BeautyServiceDto {
    private String name;
    private String description;
    private double price;
    private int duration;

    public String getName() {
        return name;
    }

    public BeautyServiceDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BeautyServiceDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public BeautyServiceDto setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public BeautyServiceDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
