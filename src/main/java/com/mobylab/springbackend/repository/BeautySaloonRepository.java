package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.BeautySaloon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeautySaloonRepository extends JpaRepository<BeautySaloon, UUID> {
    Optional<List<BeautySaloon>> getBeautySaloonsByCity(String city);
}
