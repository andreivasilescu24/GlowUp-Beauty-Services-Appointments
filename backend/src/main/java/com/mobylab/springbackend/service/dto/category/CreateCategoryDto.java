package com.mobylab.springbackend.service.dto.category;

public class CreateCategoryDto {
    private String name;

    public String getName() {
        return name;
    }

    public CreateCategoryDto setName(String name) {
        this.name = name;
        return this;
    }
}
