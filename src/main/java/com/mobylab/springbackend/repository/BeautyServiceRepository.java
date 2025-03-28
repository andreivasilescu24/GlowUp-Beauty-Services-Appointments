package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.BeautyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeautyServiceRepository extends JpaRepository<BeautyService, UUID> {
    Optional<List<BeautyService>> getBeautyServicesByBeautySalon(BeautySalon beautySalon);
    Optional<BeautyService> getBeautyServiceByIdAndBeautySalon(UUID id, BeautySalon beautySalon);
    Optional<BeautyService> getBeautyServiceById(UUID id);
}
