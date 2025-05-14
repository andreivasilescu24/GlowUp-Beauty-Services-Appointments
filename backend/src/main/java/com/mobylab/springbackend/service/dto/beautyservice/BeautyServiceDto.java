package com.mobylab.springbackend.service.dto.beautyservice;

import java.util.UUID;

public class BeautyServiceDto {
    private UUID id;
    private String name;
    private String description;

    public UUID getId() {
        return id;
    }

    public BeautyServiceDto setId(UUID id) {
        this.id = id;
        return this;
    }

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
