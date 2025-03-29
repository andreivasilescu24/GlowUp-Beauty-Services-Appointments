package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.service.EmployeeService;
import com.mobylab.springbackend.service.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/{salon_id}/employees")
public class EmployeesController {
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
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID salon_id, @PathVariable UUID employee_id, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateSalonEmployee(salon_id, employee_id, employeeDto);
        return ResponseEntity.status(200).body(updatedEmployee);
    }
}
