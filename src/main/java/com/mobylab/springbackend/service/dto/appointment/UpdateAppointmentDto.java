package com.mobylab.springbackend.service.dto.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateAppointmentDto {
    private UUID beautyServiceId;
    private UUID employeeId;
    private LocalDateTime appointmentDateAndTime;
    private int statusId;

    public UUID getBeautyServiceId() {
        return beautyServiceId;
    }

    public UpdateAppointmentDto setBeautyServiceId(UUID beautyServiceId) {
        this.beautyServiceId = beautyServiceId;
        return this;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public UpdateAppointmentDto setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public LocalDateTime getAppointmentDateAndTime() {
        return appointmentDateAndTime;
    }

    public UpdateAppointmentDto setAppointmentDateAndTime(LocalDateTime appointmentDateAndTime) {
        this.appointmentDateAndTime = appointmentDateAndTime;
        return this;
    }

    public int getStatusId() {
        return statusId;
    }

    public UpdateAppointmentDto setStatusId(int statusId) {
        this.statusId = statusId;
        return this;
    }
}
