package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.entity.WorkingHours;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.EmployeesRepository;
import com.mobylab.springbackend.repository.WorkingHoursRepository;
import com.mobylab.springbackend.service.dto.workinghours.CreateWorkingHoursDto;
import com.mobylab.springbackend.service.dto.workinghours.WorkingHoursDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;
    private final EmployeesRepository employeesRepository;

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository, EmployeesRepository employeesRepository) {
        this.workingHoursRepository = workingHoursRepository;
        this.employeesRepository = employeesRepository;
    }

    private void checkHoursValability(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Start time must be before end time");
        }
    }

    public List<WorkingHoursDto> getEmployeeWorkingHours(UUID employeeId) {
        Employee employee = employeesRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }
        Optional<List<WorkingHours>> employeeWorkingHours = workingHoursRepository.findByEmployee(employee);

        return employeeWorkingHours.map(workingHours -> workingHours
                .stream()
                .map(dayWorkingHours ->
                        new WorkingHoursDto()
                                .setWorkingHoursId(dayWorkingHours.getId())
                                .setStartTime(dayWorkingHours.getStartTime())
                                .setEndTime(dayWorkingHours.getEndTime())
                                .setDay(dayWorkingHours.getDay())
                ).collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }


    public WorkingHoursDto getEmployeeWorkingHoursById(UUID employeeId, UUID workingHoursId) {
        Employee employee = employeesRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }

        Optional<WorkingHours> dayWorkingHours = workingHoursRepository.findByEmployeeAndId(employee, workingHoursId);

        if (dayWorkingHours.isEmpty()) {
            throw new ResourceNotFoundException("Working hours corresponding to this ID not found");
        }

        WorkingHours workingHours = dayWorkingHours.get();
        return new WorkingHoursDto()
                .setWorkingHoursId(workingHours.getId())
                .setStartTime(workingHours.getStartTime())
                .setEndTime(workingHours.getEndTime())
                .setDay(workingHours.getDay());
    }

    public WorkingHoursDto addEmployeeWorkingHours(UUID employeeId, CreateWorkingHoursDto createWorkingHoursDto) {
        Employee employee = employeesRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }

        Optional<WorkingHours> workingHoursByDay = workingHoursRepository.findByEmployeeAndDay(employee, createWorkingHoursDto.getDay());

        if (workingHoursByDay.isPresent()) {
            throw new BadRequestException("Working hours for this day already exist");
        }

        checkHoursValability(createWorkingHoursDto.getStartTime(), createWorkingHoursDto.getEndTime());

        WorkingHours workingHours = new WorkingHours();
        workingHours.setEmployee(employee);
        workingHours.setStartTime(createWorkingHoursDto.getStartTime());
        workingHours.setEndTime(createWorkingHoursDto.getEndTime());
        workingHours.setDay(createWorkingHoursDto.getDay());

        workingHoursRepository.save(workingHours);

        return new WorkingHoursDto()
                .setWorkingHoursId(workingHours.getId())
                .setStartTime(workingHours.getStartTime())
                .setEndTime(workingHours.getEndTime())
                .setDay(workingHours.getDay());
    }

    public WorkingHoursDto updateEmployeeWorkingHours(UUID employeeId, UUID workingHoursId, CreateWorkingHoursDto createWorkingHoursDto) {
        Employee employee = employeesRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }

        WorkingHours workingHours = workingHoursRepository.findByIdAndEmployee(workingHoursId, employee).orElse(null);

        if (workingHours == null) {
            throw new ResourceNotFoundException("Working hours entry not found or does not belong to this employee");
        }

        Optional<WorkingHours> workingHoursByDay = workingHoursRepository.findByEmployeeAndDay(employee, createWorkingHoursDto.getDay());

        if (workingHoursByDay.isPresent()) {
            throw new BadRequestException("Working hours for this day already exist");
        }

        checkHoursValability(createWorkingHoursDto.getStartTime(), createWorkingHoursDto.getEndTime());

        workingHours.setStartTime(createWorkingHoursDto.getStartTime());
        workingHours.setEndTime(createWorkingHoursDto.getEndTime());
        workingHours.setDay(createWorkingHoursDto.getDay());
        workingHoursRepository.save(workingHours);

        return new WorkingHoursDto()
                .setWorkingHoursId(workingHours.getId())
                .setStartTime(workingHours.getStartTime())
                .setEndTime(workingHours.getEndTime())
                .setDay(workingHours.getDay());
    }

    public void deleteEmployeeWorkingHours(UUID employeeId, UUID workingHoursId) {
        Employee employee = employeesRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }

        WorkingHours workingHours = workingHoursRepository.findByIdAndEmployee(workingHoursId, employee).orElse(null);

        if (workingHours == null) {
            throw new ResourceNotFoundException("Working hours entry not found or does not belong to this employee");
        }
    }
}
