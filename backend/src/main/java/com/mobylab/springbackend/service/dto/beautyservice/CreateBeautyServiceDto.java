package com.mobylab.springbackend.service.dto.beautyservice;

import java.util.UUID;

public class CreateBeautyServiceDto {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public CreateBeautyServiceDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateBeautyServiceDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
