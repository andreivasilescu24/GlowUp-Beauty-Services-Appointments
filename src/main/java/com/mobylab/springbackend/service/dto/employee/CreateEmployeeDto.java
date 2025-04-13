package com.mobylab.springbackend.service.dto.employee;

public class CreateEmployeeDto {
    private String name;
    private int experience;
    private String phone;

    public String getName() {
        return name;
    }

    public CreateEmployeeDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getExperience() {
        return experience;
    }

    public CreateEmployeeDto setExperience(int experience) {
        this.experience = experience;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public CreateEmployeeDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
