package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.*;
import com.mobylab.springbackend.service.dto.appointment.AppointmentDto;
import com.mobylab.springbackend.service.dto.appointment.AvailableSlotDto;
import com.mobylab.springbackend.service.dto.appointment.CreateAppointmentDto;
import com.mobylab.springbackend.service.dto.appointment.UpdateAppointmentDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final BeautySalonRepository beautySalonRepository;
    private final EmployeesRepository employeesRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final UserRepository userRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final EmployeeAvailableServiceRepository employeeAvailableServiceRepository;

    private final EmailService emailService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              BeautySalonRepository beautySalonRepository,
                              EmployeesRepository employeesRepository,
                              BeautyServiceRepository beautyServiceRepository,
                              AppointmentStatusRepository appointmentStatusRepository,
                              UserRepository userRepository,
                              WorkingHoursRepository workingHoursRepository,
                              EmployeeAvailableServiceRepository employeeAvailableServiceRepository,
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.beautySalonRepository = beautySalonRepository;
        this.employeesRepository = employeesRepository;
        this.beautyServiceRepository = beautyServiceRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.userRepository = userRepository;
        this.workingHoursRepository = workingHoursRepository;
        this.employeeAvailableServiceRepository = employeeAvailableServiceRepository;
        this.emailService = emailService;
    }

    private void checkAppointmentAvailability(LocalDateTime appointmentDateAndTime, Employee employee, int serviceDuration) {
        // Calculate the end time of the new appointment
        LocalDateTime appointmentEndTime = appointmentDateAndTime.plusMinutes(serviceDuration);

        // Retrieve all appointments for the employee
        List<Appointment> employeeAppointments = appointmentRepository.findAppointmentsByEmployeeAndDate(employee.getId(), appointmentDateAndTime.toLocalDate());

        // Check for time conflicts
        for (Appointment existingAppointment : employeeAppointments) {
            LocalDateTime existingStartTime = existingAppointment.getAppointmentDateAndTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(
                    employeeAvailableServiceRepository
                            .findEmployeeAvailableServiceById(
                                    new EmployeeAvailableService.EmployeeAvailableServiceId(
                                            existingAppointment.getEmployee().getId(),
                                            existingAppointment.getBeautyService().getId()
                                    )
                            )
                            .orElseThrow(() -> new ResourceNotFoundException("Service duration not found"))
                            .getDuration()
            );

            // Check if the new appointment overlaps with the existing one
            if (appointmentDateAndTime.isBefore(existingEndTime) && appointmentEndTime.isAfter(existingStartTime)) {
                throw new BadRequestException("Appointment time is not available");
            }
        }
    }

    public List<AppointmentDto> getMyAppointments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User authUser = userRepository.findUserByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<List<Appointment>> appointments = appointmentRepository.findByClient(authUser);

        return appointments.map(user_appointments -> user_appointments
                        .stream().map(appointment -> new AppointmentDto()
                                .setId(appointment.getId())
                                .setBeautySalonName(appointment.getBeautySalon().getName())
                                .setEmployeeName(appointment.getEmployee().getName())
                                .setBeautyServiceName(appointment.getBeautyService().getName())
                                .setAppointmentDateAndTime(appointment.getAppointmentDateAndTime())
                                .setAppointmentStatus(appointment.getStatus().getName()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    public AppointmentDto createAppointment(CreateAppointmentDto createAppointmentDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        User authUser = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BeautySalon beautySalon = beautySalonRepository.getBeautySalonById(createAppointmentDto.getBeautySalonId())
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));
        BeautyService beautyService = beautyServiceRepository.findById(createAppointmentDto.getBeautyServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Beauty service not found"));
        Employee employee = employeesRepository.findById(createAppointmentDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Optional<EmployeeAvailableService> employeeService = employeeAvailableServiceRepository
                .findEmployeeAvailableServiceById(
                        new EmployeeAvailableService.EmployeeAvailableServiceId(createAppointmentDto.getEmployeeId(),
                                createAppointmentDto.getBeautyServiceId()));

        if (employeeService.isEmpty()) {
            throw new ResourceNotFoundException("Employee does not provide this service");
        }

        LocalDateTime appointmentDateAndTime = createAppointmentDto.getAppointmentDateAndTime();

        Appointment appointment = new Appointment()
                .setClient(authUser)
                .setBeautySalon(beautySalon)
                .setBeautyService(beautyService)
                .setEmployee(employee)
                .setAppointmentDateAndTime(appointmentDateAndTime);


        checkAppointmentAvailability(appointmentDateAndTime, employee, employeeService.get().getDuration());

        appointmentRepository.save(appointment);

        String timeFormatted = String.format("%02d:%02d",
                appointmentDateAndTime.getHour(),
                appointmentDateAndTime.getMinute());

        String subject = "Appointment Confirmation";
        String text = "Hi " + authUser.getName() + ",\n\n" +
                "Your appointment at " + beautySalon.getName() + " is confirmed for " +
                appointmentDateAndTime.getDayOfWeek().name().substring(0, 1).toUpperCase() +
                appointmentDateAndTime.getDayOfWeek().name().substring(1).toLowerCase() +
                ", " + appointmentDateAndTime.toLocalDate() +
                " at " + timeFormatted + ".\n\n" +
                "Thank you for choosing GlowUp!";

        emailService.sendEmail(userEmail, subject, text);

        return new AppointmentDto()
                .setId(appointment.getId())
                .setBeautySalonName(appointment.getBeautySalon().getName())
                .setEmployeeName(appointment.getEmployee().getName())
                .setBeautyServiceName(appointment.getBeautyService().getName())
                .setAppointmentDateAndTime(appointment.getAppointmentDateAndTime())
                .setAppointmentStatus(appointment.getStatus().getName());
    }

    public List<AvailableSlotDto> getAvailableSlots(UUID employeeId, UUID serviceId, UUID beautySalonId, LocalDate date) {
        // Validate beauty salon
        BeautySalon beautySalon = beautySalonRepository.findById(beautySalonId)
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));

        Employee employee = employeesRepository.findByBeautySalonAndId(beautySalon, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or not available in this salon"));

        // Validate employee and fetch working hours
        WorkingHours workingHours = workingHoursRepository.findByEmployeeAndDay(employee, date.getDayOfWeek().getValue())
                .orElseThrow(() -> new ResourceNotFoundException("Working hours not found for the employee"));

        // Fetch service duration
        int serviceDuration = employeeAvailableServiceRepository
                .findEmployeeAvailableServiceById(new EmployeeAvailableService.EmployeeAvailableServiceId(employeeId, serviceId))
                .orElseThrow(() -> new ResourceNotFoundException("Service not found for the employee"))
                .getDuration();

        // Fetch all appointments for the employee on the given day
        List<Appointment> appointments = appointmentRepository.findAppointmentsByEmployeeAndDate(employeeId, date);

        // Calculate available time slots
        LocalDateTime startTime = date.atTime(workingHours.getStartTime().getHour(), workingHours.getStartTime().getMinute());
        LocalDateTime endTime = date.atTime(workingHours.getEndTime().getHour(), workingHours.getEndTime().getMinute());

        List<AvailableSlotDto> availableSlots = new ArrayList<>();
        while (startTime.plusMinutes(serviceDuration).isBefore(endTime) || startTime.plusMinutes(serviceDuration).equals(endTime)) {
            LocalDateTime slotStartTime = startTime;
            LocalDateTime slotEndTime = startTime.plusMinutes(serviceDuration);

            boolean isAvailable = appointments.stream().noneMatch(appointment -> {
                LocalDateTime appointmentStart = appointment.getAppointmentDateAndTime();
                LocalDateTime appointmentEnd = appointmentStart.plusMinutes(
                        employeeAvailableServiceRepository
                                .findEmployeeAvailableServiceById(
                                        new EmployeeAvailableService.EmployeeAvailableServiceId(
                                                appointment.getEmployee().getId(),
                                                appointment.getBeautyService().getId()
                                        )
                                )
                                .orElseThrow(() -> new ResourceNotFoundException("Service not found for the employee"))
                                .getDuration()
                );
                return slotStartTime.isBefore(appointmentEnd) && slotEndTime.isAfter(appointmentStart);
            });

            if (isAvailable) {
                availableSlots.add(new AvailableSlotDto().setStartTime(startTime));
            }

            startTime = startTime.plusMinutes(serviceDuration); // Increment by `duration` minutes for the next slot
        }

        return availableSlots;
    }

    public AppointmentDto updateAppointmentDetails(UpdateAppointmentDto appointmentDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        throw new UnsupportedOperationException();
    }
}
