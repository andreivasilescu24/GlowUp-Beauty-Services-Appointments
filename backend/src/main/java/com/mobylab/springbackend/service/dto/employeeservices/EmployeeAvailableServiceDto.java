package com.mobylab.springbackend.service.dto.employeeservices;

import java.util.UUID;

public class EmployeeAvailableServiceDto {
    private UUID employeeId;
    private UUID serviceId;
    private String serviceName;
    private String description;
    private double price;
    private int duration;

    public UUID getEmployeeId() {
        return employeeId;
    }

    public EmployeeAvailableServiceDto setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public EmployeeAvailableServiceDto setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public EmployeeAvailableServiceDto setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EmployeeAvailableServiceDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public EmployeeAvailableServiceDto setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public EmployeeAvailableServiceDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
