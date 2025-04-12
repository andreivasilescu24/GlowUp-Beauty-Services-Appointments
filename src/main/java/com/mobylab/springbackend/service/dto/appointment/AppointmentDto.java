package com.mobylab.springbackend.service.dto.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentDto {
    private UUID id;
    private String beautySalonName;
    private String beautyServiceName;
    private String employeeName;
    private LocalDateTime appointmentDateAndTime;
    private String appointmentStatus;

    public UUID getId() {
        return id;
    }

    public AppointmentDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getBeautySalonName() {
        return beautySalonName;
    }

    public AppointmentDto setBeautySalonName(String beautySalonName) {
        this.beautySalonName = beautySalonName;
        return this;
    }

    public String getBeautyServiceName() {
        return beautyServiceName;
    }

    public AppointmentDto setBeautyServiceName(String beautyServiceName) {
        this.beautyServiceName = beautyServiceName;
        return this;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public AppointmentDto setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
        return this;
    }

    public LocalDateTime getAppointmentDateAndTime() {
        return appointmentDateAndTime;
    }

    public AppointmentDto setAppointmentDateAndTime(LocalDateTime appointmentDateAndTime) {
        this.appointmentDateAndTime = appointmentDateAndTime;
        return this;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public AppointmentDto setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
        return this;
    }
}
