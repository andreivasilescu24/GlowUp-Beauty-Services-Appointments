package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.WorkingHoursService;
import com.mobylab.springbackend.service.dto.workinghours.CreateWorkingHoursDto;
import com.mobylab.springbackend.service.dto.workinghours.WorkingHoursDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/working-hours/{employeeId}")
public class WorkingHoursController implements SecuredRestController {
    private final WorkingHoursService workingHoursService;

    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

    @GetMapping
    public ResponseEntity<List<WorkingHoursDto>> getEmployeeWorkingHours(@PathVariable UUID employeeId) {
        List<WorkingHoursDto> workingHours = workingHoursService.getEmployeeWorkingHours(employeeId);
        return ResponseEntity.status(200).body(workingHours);
    }

    @GetMapping("/{workingHoursId}")
    public ResponseEntity<WorkingHoursDto> getEmployeeWorkingHoursById(@PathVariable UUID employeeId, @PathVariable UUID workingHoursId) {
        WorkingHoursDto workingHours = workingHoursService.getEmployeeWorkingHoursById(employeeId, workingHoursId);
        return ResponseEntity.status(200).body(workingHours);
    }

    @PostMapping
    public ResponseEntity<WorkingHoursDto> addEmployeeWorkingHours(@PathVariable UUID employeeId, @RequestBody CreateWorkingHoursDto createWorkingHoursDto) {
        WorkingHoursDto createdWorkingHours = workingHoursService.addEmployeeWorkingHours(employeeId, createWorkingHoursDto);
        return ResponseEntity.status(201).body(createdWorkingHours);
    }

    @PutMapping("/{workingHoursId}")
    public ResponseEntity<WorkingHoursDto> updateEmployeeWorkingHours(@PathVariable UUID employeeId,
                                                                  @PathVariable UUID workingHoursId,
                                                                  @RequestBody CreateWorkingHoursDto createWorkingHoursDto) {
        WorkingHoursDto updatedWorkingHours = workingHoursService.updateEmployeeWorkingHours(employeeId, workingHoursId, createWorkingHoursDto);
        return ResponseEntity.status(200).body(updatedWorkingHours);
    }

    @DeleteMapping("/{workingHoursId}")
    public ResponseEntity<String> deleteEmployeeWorkingHours(@PathVariable UUID employeeId, @PathVariable UUID workingHoursId) {
        workingHoursService.deleteEmployeeWorkingHours(employeeId, workingHoursId);
        return ResponseEntity.noContent().build();
    }
}
