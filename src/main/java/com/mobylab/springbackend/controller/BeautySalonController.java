package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.service.BeautySalonService;
import com.mobylab.springbackend.service.dto.beautysalon.BeautySalonDto;
import com.mobylab.springbackend.service.dto.beautysalon.CreateBeautySalonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/add")
    public ResponseEntity<BeautySalon> addBeautySalon(@RequestBody CreateBeautySalonDto beautySalonDto) {
        BeautySalon beautySalon = beautySalonService.addBeautySalon(beautySalonDto);
        return ResponseEntity.status(201).body(beautySalon);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBeautySalon(@PathVariable UUID id) {
        logger.info("Request to delete a salon with id: " + id);
        beautySalonService.deleteBeautySalon(id);
        logger.info("Done " + id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BeautySalon> updateBeautySalon(@PathVariable UUID id, @RequestBody CreateBeautySalonDto beautySalonDto) {
        BeautySalon beautySalon = beautySalonService.updateBeautySalon(id, beautySalonDto);
        return ResponseEntity.status(200).body(beautySalon);
    }
}
