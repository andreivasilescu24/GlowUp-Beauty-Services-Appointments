package com.mobylab.springbackend.service.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateAppointmentDto {
    private UUID beautyServiceId;
    private UUID employeeId;
    @Schema(description = "The date and time of the appointment in the format yyyy-MM-dd'T'HH:mm",
            example = "2025-03-14T12:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
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
