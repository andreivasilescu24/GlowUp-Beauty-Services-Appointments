package com.mobylab.springbackend.service.dto.workinghours;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public class CreateWorkingHoursDto {
    @Schema(type = "string", example = "12:00")
    private LocalTime startTime;
    @Schema(type = "string", example = "18:00")
    private LocalTime endTime;
    @Schema(type = "integer", example = "1", description = "Day of the week (1 for Monday, 7 for Sunday)")
    private int day;

    public LocalTime getStartTime() {
        return startTime;
    }

    public CreateWorkingHoursDto setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public CreateWorkingHoursDto setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public int getDay() {
        return day;
    }

    public CreateWorkingHoursDto setDay(int day) {
        this.day = day;
        return this;
    }
}
