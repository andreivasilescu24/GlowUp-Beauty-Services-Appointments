package com.mobylab.springbackend.service.dto;

public class BeautyServiceDto {
    private String name;
    private String description;

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
}
