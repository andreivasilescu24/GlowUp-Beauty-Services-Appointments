package com.mobylab.springbackend.service.dto;

import java.util.UUID;

public class CategoryDto {
    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public CategoryDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }
}
