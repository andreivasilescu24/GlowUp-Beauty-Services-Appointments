package com.mobylab.springbackend.service.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentDto {
    private UUID id;
    private String beautySalonName;
    private String beautyServiceName;
    private String employeeName;
    @Schema(description = "The date and time of the appointment in the format yyyy-MM-dd'T'HH:mm",
            example = "2025-03-14T12:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
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
