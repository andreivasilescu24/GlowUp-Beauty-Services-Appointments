package com.mobylab.springbackend.service.dto.workinghours;

import java.time.LocalTime;
import java.util.UUID;

public class WorkingHoursDto {
    private UUID workingHoursId;
    private LocalTime endTime;
    private LocalTime startTime;
    private int day;

    public UUID getWorkingHoursId() {
        return workingHoursId;
    }

    public WorkingHoursDto setWorkingHoursId(UUID workingHoursId) {
        this.workingHoursId = workingHoursId;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public WorkingHoursDto setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public WorkingHoursDto setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public int getDay() {
        return day;
    }

    public WorkingHoursDto setDay(int day) {
        this.day = day;
        return this;
    }
}
