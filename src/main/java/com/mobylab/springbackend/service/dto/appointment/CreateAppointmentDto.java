package com.mobylab.springbackend.service.dto.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateAppointmentDto {
    private UUID beautySalonId;
    private UUID beautyServiceId;
    private UUID employeeId;
    private LocalDateTime appointmentDateAndTime;

    public UUID getBeautySalonId() {
        return beautySalonId;
    }

    public CreateAppointmentDto setBeautySalonId(UUID beautySalonId) {
        this.beautySalonId = beautySalonId;
        return this;
    }

    public UUID getBeautyServiceId() {
        return beautyServiceId;
    }

    public CreateAppointmentDto setBeautyServiceId(UUID beautyServiceId) {
        this.beautyServiceId = beautyServiceId;
        return this;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public CreateAppointmentDto setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public LocalDateTime getAppointmentDateAndTime() {
        return appointmentDateAndTime;
    }

    public CreateAppointmentDto setAppointmentDateAndTime(LocalDateTime appointmentDateAndTime) {
        this.appointmentDateAndTime = appointmentDateAndTime;
        return this;
    }
}
