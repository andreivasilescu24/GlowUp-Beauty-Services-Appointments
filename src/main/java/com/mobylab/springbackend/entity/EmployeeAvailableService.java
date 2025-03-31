package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
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

        public UUID getEmployeeId() {
            return employeeId;
        }

        public UUID getServiceId() {
            return serviceId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmployeeAvailableServiceId that = (EmployeeAvailableServiceId) o;
            return Objects.equals(employeeId, that.employeeId) &&
                    Objects.equals(serviceId, that.serviceId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(employeeId, serviceId);
        }
    }

    @EmbeddedId
    private EmployeeAvailableServiceId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id", nullable = false)
    private BeautyService service;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration;

    public EmployeeAvailableService() {}

    public EmployeeAvailableService(Employee employee, BeautyService service, double price, int duration) {
        this.id = new EmployeeAvailableServiceId(employee.getId(), service.getId());
        this.employee = employee;
        this.service = service;
        setPrice(price);
        setDuration(duration);
    }

    public EmployeeAvailableServiceId getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public BeautyService getService() {
        return service;
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
