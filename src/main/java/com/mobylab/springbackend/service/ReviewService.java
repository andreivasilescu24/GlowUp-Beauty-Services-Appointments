package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.review.CreateReviewDto;
import com.mobylab.springbackend.service.dto.review.ReviewDto;
import com.mobylab.springbackend.service.dto.review.UpdateReviewDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BeautySalonRepository beautySalonRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         BeautySalonRepository beautySalonRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.beautySalonRepository = beautySalonRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewDto> getAllReviews(UUID salonId) {
        BeautySalon beautySalon = beautySalonRepository.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Beauty salon not found"));
        Optional<List<Review>> beautySalonReviews = reviewRepository.getReviewsByBeautySalon(beautySalon);
        return beautySalonReviews.map(reviews -> reviews.stream()
                .map(review -> new ReviewDto()
                        .setId(review.getId())
                        .setComment(review.getComment())
                        .setRating(review.getRating())
                        .setCreatedAt(review.getCreatedAt())
                        .setClientId(review.getClient().getName()))
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public Review addReview(UUID salonId, CreateReviewDto createReviewDto) {
        User client = userRepository.findById(createReviewDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        BeautySalon salon = beautySalonRepository.findById(salonId)
                .orElseThrow(() -> new ResourceNotFoundException("Salon not found"));

        Review review = new Review();
        review.setClient(client);
        review.setBeautySalon(salon);
        review.setRating(createReviewDto.getRating());
        review.setComment(createReviewDto.getComment());
        review.setCreatedAt(LocalDateTime.now(Clock.systemDefaultZone()));

        return reviewRepository.save(review);
    }

    public Review updateReview(UUID salonId, UUID reviewId, UpdateReviewDto updateReviewDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getBeautySalon().getId().equals(salonId)) {
            throw new ResourceNotFoundException("Review does not belong to this salon");
        }

        review.setRating(updateReviewDto.getRating());
        review.setComment(updateReviewDto.getComment());

        return reviewRepository.save(review);
    }

    public void deleteReview(UUID salonId, UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getBeautySalon().getId().equals(salonId)) {
            throw new ResourceNotFoundException("Review does not belong to this salon");
        }

        reviewRepository.delete(review);
    }
}
