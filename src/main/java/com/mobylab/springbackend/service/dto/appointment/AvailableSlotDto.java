package com.mobylab.springbackend.service.dto.appointment;

import java.time.LocalDateTime;

public class AvailableSlotDto {
    private LocalDateTime startTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AvailableSlotDto setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
}