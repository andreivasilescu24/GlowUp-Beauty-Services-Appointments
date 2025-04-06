package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.EmployeeAvailableService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeAvailableServiceRepository extends JpaRepository<EmployeeAvailableService, EmployeeAvailableService.EmployeeAvailableServiceId> {
    Optional<List<EmployeeAvailableService>> findEmployeeServicesById_ServiceId(UUID idServiceId);
    Optional<List<EmployeeAvailableService>> findEmployeeServiceById_EmployeeId(UUID idEmployeeId);

    Optional<EmployeeAvailableService> findEmployeeAvailableServiceById(EmployeeAvailableService.EmployeeAvailableServiceId id);
}
