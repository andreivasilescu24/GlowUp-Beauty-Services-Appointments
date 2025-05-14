package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.entity.EmployeeAvailableService;
import com.mobylab.springbackend.service.EmployeeService;
import com.mobylab.springbackend.service.dto.employee.CreateEmployeeDto;
import com.mobylab.springbackend.service.dto.employeeservices.CreateEmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.employee.EmployeeDto;
import com.mobylab.springbackend.service.dto.employeeservices.EmployeeAvailableServiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon/{salon_id}/employees")
public class EmployeesController implements SecuredRestController {
    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
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
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<EmployeeDto> addEmployee(@PathVariable UUID salon_id, @RequestBody CreateEmployeeDto createEmployeeDto) {
        EmployeeDto createdEmployee = employeeService.addSalonEmployee(salon_id, createEmployeeDto);
        return ResponseEntity.status(201).body(createdEmployee);
    }

    @DeleteMapping("/{employee_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID salon_id, @PathVariable UUID employee_id) {
        employeeService.deleteSalonEmployee(salon_id, employee_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{employee_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable UUID salon_id,
                                                   @PathVariable UUID employee_id,
                                                   @RequestBody CreateEmployeeDto createEmployeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateSalonEmployee(salon_id, employee_id, createEmployeeDto);
        return ResponseEntity.status(200).body(updatedEmployee);
    }

    // EMPLOYEE SERVICES

    @PostMapping("/{employee_id}/services/{service_id}")
    public ResponseEntity<EmployeeAvailableServiceDto> getEmployeeServices(@PathVariable UUID salon_id,
                                                                        @PathVariable UUID employee_id,
                                                                        @PathVariable UUID service_id,
                                                                        @RequestBody CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        EmployeeAvailableServiceDto employeeAvailableService = this.employeeService.addServiceToEmployee(salon_id, employee_id, service_id, createEmployeeAvailableServiceDto);
        return ResponseEntity.status(201).body(employeeAvailableService);
    }

    @GetMapping("/{employee_id}/services")
    public ResponseEntity<List<EmployeeAvailableServiceDto>> getEmployeeServicesList(@PathVariable UUID salon_id,
                                                                                     @PathVariable UUID employee_id) {
        List<EmployeeAvailableServiceDto> employeeServList = employeeService.getEmployeeServiceList(salon_id, employee_id);
        return ResponseEntity.status(200).body(employeeServList);
    }

    @DeleteMapping("/{employee_id}/services/{service_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<String> deleteEmployeeService(@PathVariable UUID salon_id,
                                                        @PathVariable UUID employee_id,
                                                        @PathVariable UUID service_id) {
        employeeService.deleteEmployeeService(salon_id, employee_id, service_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{employee_id}/services/{service_id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<EmployeeAvailableServiceDto> updateEmployeeService(@PathVariable UUID salon_id,
                                                                          @PathVariable UUID employee_id,
                                                                          @PathVariable UUID service_id,
                                                                          @RequestBody CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        EmployeeAvailableServiceDto updatedEmployeeAvailableService = this.employeeService.updateEmployeeService(salon_id, employee_id, service_id, createEmployeeAvailableServiceDto);
        return ResponseEntity.status(200).body(updatedEmployeeAvailableService);
    }
}
