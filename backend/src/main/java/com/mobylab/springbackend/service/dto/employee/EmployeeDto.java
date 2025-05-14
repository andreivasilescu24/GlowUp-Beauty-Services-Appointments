package com.mobylab.springbackend.service.dto.employee;

import java.util.UUID;

public class EmployeeDto {
    private UUID id;
    private UUID salonId;
    private String name;
    private int experience;
    private String phone;

    public UUID getId() {
        return id;
    }

    public EmployeeDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getSalonId() {
        return salonId;
    }

    public EmployeeDto setSalonId(UUID salonId) {
        this.salonId = salonId;
        return this;
    }

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
