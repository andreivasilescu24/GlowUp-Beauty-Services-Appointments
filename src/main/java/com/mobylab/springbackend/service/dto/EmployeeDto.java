package com.mobylab.springbackend.service.dto;

public class EmployeeDto {
    private String name;
    private int experience;

    public String getName() {
        return name;
    }

    public EmployeeDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getExperience() {
        return experience;
    }

    public EmployeeDto setExperience(int experience) {
        this.experience = experience;
        return this;
    }
}
