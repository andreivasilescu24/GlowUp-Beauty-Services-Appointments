package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.BeautyService;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.BeautyServiceRepository;
import com.mobylab.springbackend.service.dto.BeautyServiceDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BeautyServiceManagementService {
    private final BeautyServiceRepository beautyServiceRepository;
    private final BeautySalonRepository beautySalonRepository;

    public BeautyServiceManagementService(BeautyServiceRepository beautyServiceRepository,
                                          BeautySalonRepository beautySalonRepository) {
        this.beautyServiceRepository = beautyServiceRepository;
        this.beautySalonRepository = beautySalonRepository;
    }

    private BeautySalon getCorrespondingBeautySalon(UUID salonId) {
        return beautySalonRepository.getBeautySalonById(salonId).orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));
    }

    public List<BeautyServiceDto> getBeautyServices(UUID salonId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);
        Optional<List<BeautyService>> beautyServiceList = beautyServiceRepository.getBeautyServicesByBeautySalon(beautySalon);

        return beautyServiceList.map(services -> services.stream().map(
                        service -> new BeautyServiceDto()
                                .setName(service.getName())
                                .setDescription(service.getDescription())
                ).collect(Collectors.toList())
        ).orElse(Collections.emptyList());

    }

    public BeautyServiceDto getBeautyService(UUID salonId, UUID serviceId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceByIdAndBeautySalon(serviceId, beautySalon);

        return beautyService.map(service -> new BeautyServiceDto()
                .setName(service.getName())
                .setDescription(service.getDescription())
        ).orElseThrow(() -> new ResourceNotFoundException("Beauty service not found"));
    }

    public BeautyService addBeautyService(UUID salonId, BeautyServiceDto beautyServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        BeautyService beautyService = new BeautyService();
        beautyService.setName(beautyServiceDto.getName());
        beautyService.setDescription(beautyServiceDto.getDescription());
        beautyService.setBeautySalon(beautySalon);

        return beautyServiceRepository.save(beautyService);
    }

    public void deleteBeautyService(UUID salonId, UUID serviceId) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceByIdAndBeautySalon(serviceId, beautySalon);

        if (beautyService.isPresent()) {
            beautyServiceRepository.delete(beautyService.get());
        } else {
            throw new ResourceNotFoundException("Beauty service not found");
        }
    }

    public BeautyService updateBeautyService(UUID salonId, UUID serviceId, BeautyServiceDto beautyServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceByIdAndBeautySalon(serviceId, beautySalon);
        if (beautyService.isPresent()) {
            BeautyService serviceToUpdate = beautyService.get();
            serviceToUpdate.setName(beautyServiceDto.getName());
            serviceToUpdate.setDescription(beautyServiceDto.getDescription());

            return beautyServiceRepository.save(serviceToUpdate);
        } else {
            throw new ResourceNotFoundException("Beauty service not found");
        }
    }
}
