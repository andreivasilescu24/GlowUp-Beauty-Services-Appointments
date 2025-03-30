package com.mobylab.springbackend.service.dto;

public class CreateEmployeeAvailableServiceDto {
    private double price;
    private int duration;

    public int getDuration() {
        return duration;
    }

    public CreateEmployeeAvailableServiceDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public CreateEmployeeAvailableServiceDto setPrice(double price) {
        this.price = price;
        return this;
    }
}
