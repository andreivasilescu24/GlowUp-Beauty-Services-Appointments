package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeautySalonRepository extends JpaRepository<BeautySalon, UUID> {
    Optional<BeautySalon> getBeautySalonById(UUID id);

    Optional<List<BeautySalon>> getBeautySalonsByOwner(User owner);
}
