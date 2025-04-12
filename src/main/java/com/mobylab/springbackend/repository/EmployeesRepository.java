package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeesRepository extends JpaRepository<Employee, UUID> {
    Optional<List<Employee>> getEmployeesByBeautySalon(BeautySalon beautySalon);
    Optional<Employee> getEmployeeByBeautySalonAndId(BeautySalon beautySalon, UUID employeeId);

    Optional<Employee> findByBeautySalonAndId(BeautySalon beautySalon, UUID id);
}
