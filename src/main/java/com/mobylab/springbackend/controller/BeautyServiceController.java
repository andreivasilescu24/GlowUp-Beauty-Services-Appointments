package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.BeautyService;
import com.mobylab.springbackend.service.BeautyServiceManagementService;
import com.mobylab.springbackend.service.dto.beautyservice.BeautyServiceDto;
import com.mobylab.springbackend.service.dto.beautyservice.CreateBeautyServiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon/{salonId}/beautyServices")
public class BeautyServiceController implements SecuredRestController {
    private final BeautyServiceManagementService beautyServiceManagementService;

    public BeautyServiceController(BeautyServiceManagementService beautyServiceManagementService) {
        this.beautyServiceManagementService = beautyServiceManagementService;
    }

    @GetMapping
    public ResponseEntity<List<BeautyServiceDto>> getBeautyServices(@PathVariable UUID salonId) {
        List<BeautyServiceDto> beautyServiceDtoList = beautyServiceManagementService.getBeautyServices(salonId);
        return ResponseEntity.status(200).body(beautyServiceDtoList);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<BeautyServiceDto> getBeautyService(@PathVariable UUID salonId, @PathVariable UUID serviceId) {
        BeautyServiceDto beautyServiceDto = beautyServiceManagementService.getBeautyService(salonId, serviceId);
        return ResponseEntity.status(200).body(beautyServiceDto);
    }

    @PostMapping("/add")
    public ResponseEntity<BeautyServiceDto> addBeautyService(@PathVariable UUID salonId, @RequestBody CreateBeautyServiceDto createBeautyServiceDto) {
        BeautyServiceDto createdBeautyService = beautyServiceManagementService.addBeautyService(salonId, createBeautyServiceDto);
        return ResponseEntity.status(201).body(createdBeautyService);
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteBeautyService(@PathVariable UUID salonId, @PathVariable UUID serviceId) {
        beautyServiceManagementService.deleteBeautyService(salonId, serviceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<BeautyServiceDto> updateBeautyService(@PathVariable UUID salonId, @PathVariable UUID serviceId, @RequestBody CreateBeautyServiceDto createBeautyServiceDto) {
        BeautyServiceDto updatedBeautyService = beautyServiceManagementService.updateBeautyService(salonId, serviceId,  createBeautyServiceDto);
        return ResponseEntity.status(200).body(updatedBeautyService);
    }
}
