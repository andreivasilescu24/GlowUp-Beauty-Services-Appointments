package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.entity.WorkingHours;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, UUID> {
    Optional<List<WorkingHours>> findByEmployee(Employee employee);

    Optional<WorkingHours> findByEmployeeAndId(Employee employee, UUID id);

    Optional<WorkingHours> findByIdAndEmployee(UUID id, Employee employee);

    Optional<WorkingHours> findByEmployeeAndDay(Employee employee, int day);
}
