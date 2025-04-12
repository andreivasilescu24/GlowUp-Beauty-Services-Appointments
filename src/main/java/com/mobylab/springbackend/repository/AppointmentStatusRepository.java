package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
}
