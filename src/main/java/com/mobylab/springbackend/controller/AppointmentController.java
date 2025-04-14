package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.AppointmentService;
import com.mobylab.springbackend.service.dto.appointment.AppointmentDto;
import com.mobylab.springbackend.service.dto.appointment.AvailableSlotDto;
import com.mobylab.springbackend.service.dto.appointment.CreateAppointmentDto;
import com.mobylab.springbackend.service.dto.appointment.UpdateAppointmentDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController implements SecuredRestController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<AppointmentDto>> getMyAppointments() {
        List<AppointmentDto> appointments = appointmentService.getMyAppointments();
        return ResponseEntity.status(200).body(appointments);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotDto>> getAvailableSlots(
            @RequestParam UUID employeeId,
            @RequestParam UUID serviceId,
            @RequestParam UUID beautySalonId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailableSlotDto> availableSlots = appointmentService.getAvailableSlots(employeeId, serviceId, beautySalonId, date);
        return ResponseEntity.status(200).body(availableSlots);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody CreateAppointmentDto createAppointmentDto) {
        AppointmentDto createdAppointment = appointmentService.createAppointment(createAppointmentDto);
        return ResponseEntity.status(201).body(createdAppointment);
    }

    @PutMapping
    public ResponseEntity<AppointmentDto> updateAppointment(@RequestBody UpdateAppointmentDto appointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointmentDetails(appointmentDto);
        return ResponseEntity.status(200).body(updatedAppointment);
    }

}
