package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.BeautyService;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.BeautyServiceRepository;
import com.mobylab.springbackend.service.dto.beautyservice.BeautyServiceDto;
import com.mobylab.springbackend.service.dto.beautyservice.CreateBeautyServiceDto;
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

    public BeautyServiceDto addBeautyService(UUID salonId, CreateBeautyServiceDto createBeautyServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        BeautyService beautyService = new BeautyService();
        beautyService.setName(createBeautyServiceDto.getName());
        beautyService.setDescription(createBeautyServiceDto.getDescription());
        beautyService.setBeautySalon(beautySalon);

        beautyServiceRepository.save(beautyService);

        return new BeautyServiceDto()
                .setId(beautyService.getId())
                .setName(beautyService.getName())
                .setDescription(beautyService.getDescription());
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

    public BeautyServiceDto updateBeautyService(UUID salonId, UUID serviceId, CreateBeautyServiceDto createBeautyServiceDto) {
        BeautySalon beautySalon = getCorrespondingBeautySalon(salonId);

        Optional<BeautyService> beautyService = beautyServiceRepository.getBeautyServiceByIdAndBeautySalon(serviceId, beautySalon);
        if (beautyService.isPresent()) {
            BeautyService serviceToUpdate = beautyService.get();
            serviceToUpdate.setName(createBeautyServiceDto.getName());
            serviceToUpdate.setDescription(createBeautyServiceDto.getDescription());

            beautyServiceRepository.save(serviceToUpdate);

            return new BeautyServiceDto()
                    .setId(serviceId)
                    .setName(serviceToUpdate.getName())
                    .setDescription(serviceToUpdate.getDescription());
        } else {
            throw new ResourceNotFoundException("Beauty service not found");
        }
    }
}
