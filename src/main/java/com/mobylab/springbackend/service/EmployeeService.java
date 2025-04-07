package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.BeautyService;
import com.mobylab.springbackend.entity.Employee;
import com.mobylab.springbackend.entity.EmployeeAvailableService;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.BeautyServiceRepository;
import com.mobylab.springbackend.repository.EmployeeAvailableServiceRepository;
import com.mobylab.springbackend.repository.EmployeesRepository;
import com.mobylab.springbackend.service.dto.employeeservices.CreateEmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.employeeservices.EmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.employee.EmployeeDto;
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
    private final EmployeeAvailableServiceRepository employeeAvailableServiceRepository;
    private final BeautyServiceRepository beautyServiceRepository;


    public EmployeeService(EmployeesRepository employeesRepository, BeautySalonRepository beautySalonRepository,
                           EmployeeAvailableServiceRepository employeeAvailableServiceRepository,
                           BeautyServiceRepository beautyServiceRepository) {
        this.employeesRepository = employeesRepository;
        this.beautySalonRepository = beautySalonRepository;
        this.employeeAvailableServiceRepository = employeeAvailableServiceRepository;
        this.beautyServiceRepository = beautyServiceRepository;
    }

    private BeautySalon getCorrespondingBeautySalon(UUID salonId) {
        return beautySalonRepository.getBeautySalonById(salonId).orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));
    }

    private BeautyService getBeautyServiceById(UUID serviceId) {
        return beautyServiceRepository.getBeautyServiceById(serviceId).orElseThrow(() ->
                new ResourceNotFoundException("Selected beauty service doesn't exist"));
    }

    public List<EmployeeDto> getEmployeesBySalonId(UUID salonId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<List<Employee>> employeesBySalon = employeesRepository.getEmployeesByBeautySalon(beautySalon);
        return employeesBySalon.map(employees ->
                        employees.stream()
                                .map(employee -> new EmployeeDto()
                                        .setName(employee.getName())
                                        .setExperience(employee.getExperience())
                                        .setPhone(employee.getPhone()))
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public EmployeeDto getSalonEmployee(UUID salonId, UUID employeeId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            return new EmployeeDto()
                    .setName(employee.get().getName())
                    .setExperience(employee.get().getExperience())
                    .setPhone(employee.get().getPhone());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public Employee addSalonEmployee(UUID salonId, EmployeeDto employeeDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Employee employee = new Employee();
        employee.setExperience(employeeDto.getExperience());
        employee.setBeautySalon(beautySalon);
        employee.setName(employeeDto.getName());
        employee.setPhone(employeeDto.getPhone());

        return employeesRepository.save(employee);
    }

    public void deleteSalonEmployee(UUID salonId, UUID employeeId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            employeesRepository.delete(employee.get());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public Employee updateSalonEmployee(UUID salonId, UUID employeeId, EmployeeDto employeeDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setExperience(employeeDto.getExperience());
            existingEmployee.setName(employeeDto.getName());
            existingEmployee.setPhone(employeeDto.getPhone());
            return employeesRepository.save(existingEmployee);
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public EmployeeAvailableService addServiceToEmployee(UUID salonId, UUID employeeId, UUID serviceId,
                                                         CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        BeautyService beautyService = getBeautyServiceById(serviceId);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            EmployeeAvailableService.EmployeeAvailableServiceId employeeAvailableServiceId =
                    new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId);
            EmployeeAvailableService employeeAvailableService = new EmployeeAvailableService(employee.get(), beautyService,
                    createEmployeeAvailableServiceDto.getPrice(),
                    createEmployeeAvailableServiceDto.getDuration());
            return employeeAvailableServiceRepository.save(employeeAvailableService);
        } else {
            throw new ResourceNotFoundException("Employee not found or doesn't work at the selected salon");
        }
    }

    public List<EmployeeAvailableServiceDto> getEmployeeServiceList(UUID salonId, UUID employeeId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);

        if (employee.isPresent()) {
            Optional<List<EmployeeAvailableService>> employeeAvailableServices = employeeAvailableServiceRepository.findEmployeeServiceById_EmployeeId(employeeId);

            return employeeAvailableServices.map(employeeAvailableServices1 ->
                            employeeAvailableServices1.stream().map(employeeService -> new EmployeeAvailableServiceDto()
                                    .setDuration(employeeService.getDuration())
                                    .setPrice(employeeService.getPrice())
                                    .setServiceName(employeeService.getService().getName())
                                    .setDescription(employeeService.getService().getDescription())).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
        } else {
            throw new ResourceNotFoundException("Employee not found or doesn't work at the selected salon");
        }
    }


    public void deleteEmployeeService(UUID salonId, UUID employeeId, UUID serviceId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceById(serviceId);
            if (beautyService.isPresent()) {
                Optional<EmployeeAvailableService> employeeAvailableService = employeeAvailableServiceRepository
                        .findEmployeeAvailableServiceById(new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId));
                if (employeeAvailableService.isPresent()) {
                    employeeAvailableServiceRepository.delete(employeeAvailableService.get());
                } else {
                    throw new ResourceNotFoundException("Service was not found for this employee");
                }
            } else {
                throw new ResourceNotFoundException("Service not found");
            }
        } else {
            throw new ResourceNotFoundException("Employee not found or doesn't work at the selected salon");
        }
    }

    public EmployeeAvailableService updateEmployeeService(UUID salonId, UUID employeeId, UUID serviceId, CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceById(serviceId);
            if (beautyService.isPresent()) {
                Optional<EmployeeAvailableService> employeeAvailableService = employeeAvailableServiceRepository
                        .findEmployeeAvailableServiceById(new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId));
                if (employeeAvailableService.isPresent()) {
                    EmployeeAvailableService existingEmployeeAvailableService = employeeAvailableService.get();
                    existingEmployeeAvailableService.setPrice(createEmployeeAvailableServiceDto.getPrice());
                    existingEmployeeAvailableService.setDuration(createEmployeeAvailableServiceDto.getDuration());
                    return employeeAvailableServiceRepository.save(existingEmployeeAvailableService);
                } else {
                    throw new ResourceNotFoundException("Service was not found for this employee");
                }
            } else {
                throw new ResourceNotFoundException("Service not found");
            }
        } else {
            throw new ResourceNotFoundException("Employee not found or doesn't work at the selected salon");
        }
    }
}
