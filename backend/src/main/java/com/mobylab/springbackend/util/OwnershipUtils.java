package com.mobylab.springbackend.util;

import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OwnershipUtils {
    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    public static void checkSalonOwnership(BeautySalon beautySalon, UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isAdmin() && !beautySalon.getOwner().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not the owner of this beauty salon");
        }
    }

    public static void checkReviewOwnership(Review review, UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isAdmin() && !review.getClient().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not the owner of this review");
        }
    }

    public static void checkEmployeeBelongsToOwnerSalon(Employee employee, UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isAdmin() && !employee.getBeautySalon().getOwner().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not the owner of this beauty salon");
        }
    }

    public static void checkAppointmentBelongsToClient(Appointment appointment, UserRepository userRepository) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Fetch the authenticated user
        User authUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isAdmin() && !appointment.getClient().getId().equals(authUser.getId())) {
            throw new ResourceNotFoundException("You are not the one who made this appointment");
        }
    }
}