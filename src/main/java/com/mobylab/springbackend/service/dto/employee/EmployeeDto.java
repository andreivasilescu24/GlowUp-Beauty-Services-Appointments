package com.mobylab.springbackend.service.dto.employee;

public class EmployeeDto {
    private String name;
    private int experience;
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public EmployeeDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
