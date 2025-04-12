package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<List<Review>> getReviewsByBeautySalon(BeautySalon beautySalon);
}
