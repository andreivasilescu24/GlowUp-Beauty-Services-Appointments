package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.entity.EmployeeAvailableService;
import com.mobylab.springbackend.service.dto.CreateEmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.EmployeeDto;
import com.mobylab.springbackend.service.dto.EmployeeAvailableServiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon/{salon_id}/employees")
public class EmployeesController {
    private final com.mobylab.springbackend.service.EmployeeService employeeService;

    public EmployeesController(com.mobylab.springbackend.service.EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getSalonEmployees(@PathVariable UUID salon_id) {
        List<EmployeeDto> employees = employeeService.getEmployeesBySalonId(salon_id);
        return ResponseEntity.status(200).body(employees);
    }

    @GetMapping("/{employee_id}")
    public ResponseEntity<EmployeeDto> getSalonEmployeeById(@PathVariable UUID salon_id, @PathVariable UUID employee_id) {
        EmployeeDto employee = employeeService.getSalonEmployee(salon_id, employee_id);
        return ResponseEntity.status(200).body(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@PathVariable UUID salon_id, @RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.addSalonEmployee(salon_id, employeeDto);
        return ResponseEntity.status(201).body(createdEmployee);
    }

    @DeleteMapping("/{employee_id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID salon_id, @PathVariable UUID employee_id) {
        employeeService.deleteSalonEmployee(salon_id, employee_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{employee_id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID salon_id,
                                                   @PathVariable UUID employee_id,
                                                   @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateSalonEmployee(salon_id, employee_id, employeeDto);
        return ResponseEntity.status(200).body(updatedEmployee);
    }

    // EMPLOYEE SERVICES

    @PostMapping("/{employee_id}/services/{service_id}")
    public ResponseEntity<EmployeeAvailableService> getEmployeeServices(@PathVariable UUID salon_id,
                                                                        @PathVariable UUID employee_id,
                                                                        @PathVariable UUID service_id,
                                                                        @RequestBody CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        EmployeeAvailableService employeeAvailableService = this.employeeService.addServiceToEmployee(salon_id, employee_id, service_id, createEmployeeAvailableServiceDto);
        return ResponseEntity.status(201).body(employeeAvailableService);
    }

    @GetMapping("/{employee_id}/services")
    public ResponseEntity<List<EmployeeAvailableServiceDto>> getEmployeeServicesList(@PathVariable UUID salon_id,
                                                                                     @PathVariable UUID employee_id) {
        List<EmployeeAvailableServiceDto> employeeServList = employeeService.getEmployeeServiceList(salon_id, employee_id);
        return ResponseEntity.status(200).body(employeeServList);
    }

    @DeleteMapping("/{employee_id}/services/{service_id}")
    public ResponseEntity<String> deleteEmployeeService(@PathVariable UUID salon_id,
                                                        @PathVariable UUID employee_id,
                                                        @PathVariable UUID service_id) {
        employeeService.deleteEmployeeService(salon_id, employee_id, service_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{employee_id}/services/{service_id}")
    public ResponseEntity<EmployeeAvailableService> updateEmployeeService(@PathVariable UUID salon_id,
                                                                          @PathVariable UUID employee_id,
                                                                          @PathVariable UUID service_id,
                                                                          @RequestBody CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        EmployeeAvailableService updatedEmployeeAvailableService = this.employeeService.updateEmployeeService(salon_id, employee_id, service_id, createEmployeeAvailableServiceDto);
        return ResponseEntity.status(200).body(updatedEmployeeAvailableService);
    }
}
