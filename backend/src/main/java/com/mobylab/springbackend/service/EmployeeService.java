package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.*;
import com.mobylab.springbackend.service.dto.employee.CreateEmployeeDto;
import com.mobylab.springbackend.service.dto.employeeservices.CreateEmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.employeeservices.EmployeeAvailableServiceDto;
import com.mobylab.springbackend.service.dto.employee.EmployeeDto;
import com.mobylab.springbackend.util.OwnershipUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;


    public EmployeeService(EmployeesRepository employeesRepository, BeautySalonRepository beautySalonRepository,
                           EmployeeAvailableServiceRepository employeeAvailableServiceRepository,
                           BeautyServiceRepository beautyServiceRepository,
                           UserRepository userRepository) {
        this.employeesRepository = employeesRepository;
        this.beautySalonRepository = beautySalonRepository;
        this.employeeAvailableServiceRepository = employeeAvailableServiceRepository;
        this.beautyServiceRepository = beautyServiceRepository;
        this.userRepository = userRepository;
    }

    private BeautySalon getCorrespondingBeautySalon(UUID salonId) {
        return beautySalonRepository.getBeautySalonById(salonId).orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));
    }

    private BeautyService getBeautyServiceById(UUID serviceId) {
        return beautyServiceRepository.getBeautyServiceById(serviceId).orElseThrow(() ->
                new ResourceNotFoundException("Selected beauty service doesn't exist"));
    }

    private void checkDurationValidity(int serviceDuration) {
        if (serviceDuration <= 0) {
            throw new BadRequestException("Service duration must be greater than zero");
        }

        if (serviceDuration % 10 != 0) {
            throw new BadRequestException("Service duration must be a multiple of 5");
        }
    }

    private void checkPriceValidity(double price) {
        if (price < 0) {
            throw new BadRequestException("Service price cannot be negative");
        }
    }

    public List<EmployeeDto> getEmployeesBySalonId(UUID salonId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<List<Employee>> employeesBySalon = employeesRepository.getEmployeesByBeautySalon(beautySalon);
        return employeesBySalon.map(employees ->
                        employees.stream()
                                .map(employee -> new EmployeeDto()
                                        .setId(employee.getId())
                                        .setSalonId(employee.getBeautySalon().getId())
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
                    .setId(employee.get().getId())
                    .setSalonId(employee.get().getBeautySalon().getId())
                    .setName(employee.get().getName())
                    .setExperience(employee.get().getExperience())
                    .setPhone(employee.get().getPhone());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public EmployeeDto addSalonEmployee(UUID salonId, CreateEmployeeDto createEmployeeDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);
        Employee employee = new Employee();
        employee.setExperience(createEmployeeDto.getExperience());
        employee.setBeautySalon(beautySalon);
        employee.setName(createEmployeeDto.getName());
        employee.setPhone(createEmployeeDto.getPhone());

        employeesRepository.save(employee);

        return new EmployeeDto()
                .setId(employee.getId())
                .setSalonId(employee.getBeautySalon().getId())
                .setName(employee.getName())
                .setExperience(employee.getExperience())
                .setPhone(employee.getPhone());
    }

    public void deleteSalonEmployee(UUID salonId, UUID employeeId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            employeesRepository.delete(employee.get());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public EmployeeDto updateSalonEmployee(UUID salonId, UUID employeeId, CreateEmployeeDto createEmployeeDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setExperience(createEmployeeDto.getExperience());
            existingEmployee.setName(createEmployeeDto.getName());
            existingEmployee.setPhone(createEmployeeDto.getPhone());
            employeesRepository.save(existingEmployee);

            return new EmployeeDto()
                    .setId(existingEmployee.getId())
                    .setSalonId(existingEmployee.getBeautySalon().getId())
                    .setName(existingEmployee.getName())
                    .setExperience(existingEmployee.getExperience())
                    .setPhone(existingEmployee.getPhone());
        } else {
            throw new ResourceNotFoundException("Employee not found");
        }
    }

    public EmployeeAvailableServiceDto addServiceToEmployee(UUID salonId, UUID employeeId, UUID serviceId,
                                                         CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        BeautyService beautyService = getBeautyServiceById(serviceId);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            int serviceDuration = createEmployeeAvailableServiceDto.getDuration();
            double servicePrice = createEmployeeAvailableServiceDto.getPrice();

            checkDurationValidity(serviceDuration);
            checkPriceValidity(servicePrice);

            EmployeeAvailableService.EmployeeAvailableServiceId employeeAvailableServiceId =
                    new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId);
            EmployeeAvailableService employeeAvailableService =
                    new EmployeeAvailableService(
                            employee.get(),
                            beautyService,
                            servicePrice,
                            serviceDuration);
            employeeAvailableServiceRepository.save(employeeAvailableService);

            return new EmployeeAvailableServiceDto()
                    .setServiceId(employeeAvailableService.getId().getServiceId())
                    .setEmployeeId(employeeAvailableService.getId().getEmployeeId())
                    .setDuration(employeeAvailableService.getDuration())
                    .setPrice(employeeAvailableService.getPrice())
                    .setServiceName(beautyService.getName())
                    .setDescription(beautyService.getDescription());
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
                                    .setServiceId(employeeService.getId().getServiceId())
                                    .setEmployeeId(employeeService.getId().getEmployeeId())
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
        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

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

    public EmployeeAvailableServiceDto updateEmployeeService(UUID salonId, UUID employeeId, UUID serviceId, CreateEmployeeAvailableServiceDto createEmployeeAvailableServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        Optional<Employee> employee = employeesRepository.getEmployeeByBeautySalonAndId(beautySalon, employeeId);
        if (employee.isPresent()) {
            Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceById(serviceId);
            if (beautyService.isPresent()) {
                Optional<EmployeeAvailableService> employeeAvailableService = employeeAvailableServiceRepository
                        .findEmployeeAvailableServiceById(new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId));
                if (employeeAvailableService.isPresent()) {
                    int serviceDuration = createEmployeeAvailableServiceDto.getDuration();
                    double servicePrice = createEmployeeAvailableServiceDto.getPrice();

                    checkDurationValidity(serviceDuration);
                    checkPriceValidity(servicePrice);

                    EmployeeAvailableService existingEmployeeAvailableService = employeeAvailableService.get();
                    existingEmployeeAvailableService.setPrice(servicePrice);
                    existingEmployeeAvailableService.setDuration(serviceDuration);
                    employeeAvailableServiceRepository.save(existingEmployeeAvailableService);

                    return new EmployeeAvailableServiceDto()
                            .setServiceId(existingEmployeeAvailableService.getId().getServiceId())
                            .setEmployeeId(existingEmployeeAvailableService.getId().getEmployeeId())
                            .setDuration(existingEmployeeAvailableService.getDuration())
                            .setPrice(existingEmployeeAvailableService.getPrice())
                            .setServiceName(beautyService.get().getName())
                            .setDescription(beautyService.get().getDescription());
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
