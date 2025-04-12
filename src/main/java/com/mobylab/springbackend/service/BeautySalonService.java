package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.service.dto.beautysalon.BeautySalonDto;
import com.mobylab.springbackend.service.dto.beautysalon.CreateBeautySalonDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BeautySalonService {
    private final BeautySalonRepository beautySalonRepository;
    private final CategoryRepository categoryRepository;

    public BeautySalonService(BeautySalonRepository beautySalonRepository,
                              CategoryRepository categoryRepository) {
        this.beautySalonRepository = beautySalonRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<BeautySalonDto> getBeautySalons() {
        List<BeautySalon> salonList = beautySalonRepository.findAll();
        return salonList.stream().map(beautySalon ->
                new BeautySalonDto()
                        .setId(beautySalon.getId())
                        .setName(beautySalon.getName())
                        .setCity(beautySalon.getCity())
                        .setAddress(beautySalon.getAddress())
                        .setEmail(beautySalon.getEmail())
                        .setPhone(beautySalon.getPhone())
                        .setNumEmployees(beautySalon.getNumEmployees())
        ).collect(Collectors.toList());
    }

    public BeautySalonDto getBeautySalon(UUID id) {
        Optional<BeautySalon> optionalBeautySalon = beautySalonRepository.getBeautySalonById(id);
        if (optionalBeautySalon.isEmpty()) {
            throw new ResourceNotFoundException("Beauty salon not found");
        } else {
            BeautySalon beautySalon = optionalBeautySalon.get();
            return new BeautySalonDto()
                    .setId(beautySalon.getId())
                    .setName(beautySalon.getName())
                    .setCity(beautySalon.getCity())
                    .setAddress(beautySalon.getAddress())
                    .setEmail(beautySalon.getEmail())
                    .setPhone(beautySalon.getPhone())
                    .setNumEmployees(beautySalon.getNumEmployees());
        }
    }

    public BeautySalon addBeautySalon(CreateBeautySalonDto beautySalonDto) {
        Category category = categoryRepository.findById(beautySalonDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        BeautySalon beautySalon = new BeautySalon();
        beautySalon.setName(beautySalonDto.getName());
        beautySalon.setCity(beautySalonDto.getCity());
        beautySalon.setAddress(beautySalonDto.getAddress());
        beautySalon.setEmail(beautySalonDto.getEmail());
        beautySalon.setPhone(beautySalonDto.getPhone());
        beautySalon.setNumEmployees(beautySalonDto.getNumEmployees());
        beautySalon.setCategory(category);

        return beautySalonRepository.save(beautySalon);
    }

    public void deleteBeautySalon(UUID id) {
        BeautySalon beautySalon = beautySalonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));

        beautySalonRepository.delete(beautySalon);
    }

    public BeautySalon updateBeautySalon(UUID id, CreateBeautySalonDto beautySalonDto) {
        Category category = categoryRepository.findById(beautySalonDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        BeautySalon beautySalon = beautySalonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));

        beautySalon.setName(beautySalonDto.getName());
        beautySalon.setCity(beautySalonDto.getCity());
        beautySalon.setAddress(beautySalonDto.getAddress());
        beautySalon.setEmail(beautySalonDto.getEmail());
        beautySalon.setPhone(beautySalonDto.getPhone());
        beautySalon.setNumEmployees(beautySalonDto.getNumEmployees());
        beautySalon.setCategory(category);

        return beautySalonRepository.save(beautySalon);
    }
}
