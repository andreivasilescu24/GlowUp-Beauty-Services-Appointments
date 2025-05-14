package com.mobylab.springbackend.service.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class AvailableSlotDto {
    @Schema(description = "The date and time of the appointment in the format yyyy-MM-dd'T'HH:mm",
            example = "2025-03-14T12:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AvailableSlotDto setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
}