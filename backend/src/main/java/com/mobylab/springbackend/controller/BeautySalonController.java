package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.service.BeautySalonService;
import com.mobylab.springbackend.service.dto.beautysalon.BeautySalonDto;
import com.mobylab.springbackend.service.dto.beautysalon.CreateBeautySalonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon")
public class BeautySalonController implements SecuredRestController {
    private final BeautySalonService beautySalonService;
    private static final Logger logger = LoggerFactory.getLogger(BeautySalonController.class);

    public BeautySalonController(BeautySalonService beautySalonService) {
        this.beautySalonService = beautySalonService;
    }

    @GetMapping
    public ResponseEntity<List<BeautySalonDto>> getBeautySalons() {
        List<BeautySalonDto> beautySalonDtoList = beautySalonService.getBeautySalons();
        return ResponseEntity.status(200).body(beautySalonDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeautySalonDto> getBeautySalon(@PathVariable UUID id) {
        BeautySalonDto beautySalonDto = beautySalonService.getBeautySalon(id);
        return ResponseEntity.status(200).body(beautySalonDto);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<List<BeautySalonDto>> getMyBeautySalons() {
        List<BeautySalonDto> beautySalonDtoList = beautySalonService.getMyBeautySalons();
        return ResponseEntity.status(200).body(beautySalonDtoList);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<BeautySalonDto> addBeautySalon(@RequestBody CreateBeautySalonDto beautySalonDto) {
        BeautySalonDto beautySalon = beautySalonService.addBeautySalon(beautySalonDto);
        return ResponseEntity.status(201).body(beautySalon);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<String> deleteBeautySalon(@PathVariable UUID id) {
        logger.info("Request to delete a salon with id: " + id);
        beautySalonService.deleteBeautySalon(id);
        logger.info("Done " + id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<BeautySalonDto> updateBeautySalon(@PathVariable UUID id, @RequestBody CreateBeautySalonDto beautySalonDto) {
        BeautySalonDto beautySalon = beautySalonService.updateBeautySalon(id, beautySalonDto);
        return ResponseEntity.status(200).body(beautySalon);
    }
}
