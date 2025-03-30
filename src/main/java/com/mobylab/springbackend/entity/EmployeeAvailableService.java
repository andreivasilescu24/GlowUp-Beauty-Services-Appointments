package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "employee_services", schema = "project")
public class EmployeeAvailableService implements Serializable {
    @Embeddable
    public static class EmployeeAvailableServiceId implements Serializable {
        private UUID employeeId;
        private UUID serviceId;

        public EmployeeAvailableServiceId() {}

        public EmployeeAvailableServiceId(UUID employeeId, UUID serviceId) {
            this.employeeId = employeeId;
            this.serviceId = serviceId;
        }

        public void setEmployeeId(UUID employeeId) {
            this.employeeId = employeeId;
        }

        public UUID getEmployeeId() {
            return employeeId;
        }

        public void setServiceId(UUID serviceId) {
            this.serviceId = serviceId;
        }

        public UUID getServiceId() {
            return serviceId;
        }
    }

    @EmbeddedId
    private EmployeeAvailableServiceId id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration;

    public EmployeeAvailableServiceId getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive");
        this.duration = duration;
    }
}
