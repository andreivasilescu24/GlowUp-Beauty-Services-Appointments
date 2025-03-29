package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.EmployeesRepository;
import com.mobylab.springbackend.service.dto.EmployeeDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeesRepository employeesRepository;
    private final BeautySalonRepository beautySalonRepository;

    public EmployeeService(EmployeesRepository employeesRepository, BeautySalonRepository beautySalonRepository) {
        this.employeesRepository = employeesRepository;
        this.beautySalonRepository = beautySalonRepository;
    }

    private BeautySalon getCorrespondingBeautySalon(UUID salonId) {
        return beautySalonRepository.getBeautySalonById(salonId).orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));
    }

    public List<EmployeeDto> getEmployeesBySalonId(UUID salonId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<List<Employee>> employeesBySalon = employeesRepository.getEmployeesByBeautySalon(beautySalon);
        return employeesBySalon.map(employees ->
                employees.stream()
                        .map(employee -> new EmployeeDto()
                                .setName(employee.getUser().getName())
                                .setExperience(employee.getExperience()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public EmployeeDto getSalonEmployee(UUID salonId, UUID employeeId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            return new EmployeeDto()
                    .setName(employee.get().getUser().getName())
                    .setExperience(employee.get().getExperience());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public Employee addSalonEmployee(UUID salonId, EmployeeDto employeeDto) {
//        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
//        Employee employee = new Employee();
//        employee.setExperience(employeeDto.getExperience());
//        employee.setBeautySalon(beautySalon);
////        employee.setUser(emplo);
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public void deleteSalonEmployee(UUID salonId, UUID employeeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Employee updateSalonEmployee(UUID salonId, UUID employeeId, EmployeeDto employeeDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
