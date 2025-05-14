package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Appointment;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<List<Appointment>> findByClient(User client);
    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :employeeId AND DATE(a.appointmentDateAndTime) = :date")
    List<Appointment> findAppointmentsByEmployeeAndDate(@Param("employeeId") UUID employeeId, @Param("date") LocalDate date);
}
