package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.beautysalon.BeautySalonDto;
import com.mobylab.springbackend.service.dto.beautysalon.CreateBeautySalonDto;
import com.mobylab.springbackend.util.OwnershipUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public BeautySalonService(BeautySalonRepository beautySalonRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository) {
        this.beautySalonRepository = beautySalonRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
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

    public BeautySalonDto addBeautySalon(CreateBeautySalonDto beautySalonDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User owner = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
        beautySalon.setOwner(owner);

        beautySalonRepository.save(beautySalon);

        return new BeautySalonDto()
                .setId(beautySalon.getId())
                .setName(beautySalon.getName())
                .setCity(beautySalon.getCity())
                .setAddress(beautySalon.getAddress())
                .setEmail(beautySalon.getEmail())
                .setPhone(beautySalon.getPhone())
                .setNumEmployees(beautySalon.getNumEmployees());
    }

    public void deleteBeautySalon(UUID id) {
        BeautySalon beautySalon = beautySalonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));

        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        beautySalonRepository.delete(beautySalon);
    }

    public BeautySalonDto updateBeautySalon(UUID id, CreateBeautySalonDto beautySalonDto) {
        BeautySalon beautySalon = beautySalonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beauty salon not found"));

        OwnershipUtils.checkSalonOwnership(beautySalon, userRepository);

        Category category = categoryRepository.findById(beautySalonDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        beautySalon.setName(beautySalonDto.getName());
        beautySalon.setCity(beautySalonDto.getCity());
        beautySalon.setAddress(beautySalonDto.getAddress());
        beautySalon.setEmail(beautySalonDto.getEmail());
        beautySalon.setPhone(beautySalonDto.getPhone());
        beautySalon.setNumEmployees(beautySalonDto.getNumEmployees());
        beautySalon.setCategory(category);

        beautySalonRepository.save(beautySalon);

        return new BeautySalonDto()
                .setId(beautySalon.getId())
                .setName(beautySalon.getName())
                .setCity(beautySalon.getCity())
                .setAddress(beautySalon.getAddress())
                .setEmail(beautySalon.getEmail())
                .setPhone(beautySalon.getPhone())
                .setNumEmployees(beautySalon.getNumEmployees());
    }

    public List<BeautySalonDto> getMyBeautySalons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User owner = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<List<BeautySalon>> ownerBeautySalons = beautySalonRepository.getBeautySalonsByOwner(owner);

        if (ownerBeautySalons.isEmpty()) {
            throw new ResourceNotFoundException("No beauty salons found for this user");
        } else {
            return ownerBeautySalons.get().stream().map(beautySalon ->
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
    }
}
