package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment", schema = "project")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private BeautySalon beautySalon;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private BeautyService beautyService;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentDateAndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private AppointmentStatus status;

    public Appointment() {
        this.status = new AppointmentStatus();
        this.status.setId(1);
    }

    public User getClient() {
        return client;
    }

    public Appointment setClient(User client) {
        this.client = client;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Appointment setId(UUID id) {
        this.id = id;
        return this;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Appointment setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public BeautySalon getBeautySalon() {
        return beautySalon;
    }

    public Appointment setBeautySalon(BeautySalon beautySalon) {
        this.beautySalon = beautySalon;
        return this;
    }

    public BeautyService getBeautyService() {
        return beautyService;
    }

    public Appointment setBeautyService(BeautyService beautyService) {
        this.beautyService = beautyService;
        return this;
    }

    public LocalDateTime getAppointmentDateAndTime() {
        return appointmentDateAndTime;
    }

    public Appointment setAppointmentDateAndTime(LocalDateTime appointmentDateAndTime) {
        this.appointmentDateAndTime = appointmentDateAndTime;
        return this;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Appointment setStatus(AppointmentStatus status) {
        this.status = status;
        return this;
    }

}
