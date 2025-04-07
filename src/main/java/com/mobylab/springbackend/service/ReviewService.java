package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.BeautySalon;
import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.repository.BeautySalonRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.service.dto.review.ReviewDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BeautySalonRepository beautySalonRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         BeautySalonRepository beautySalonRepository) {
        this.reviewRepository = reviewRepository;
        this.beautySalonRepository = beautySalonRepository;
    }

    public List<ReviewDto> getAllReviews(UUID salonId) {
            BeautySalon beautySalon = beautySalonRepository.findById(salonId)
                    .orElseThrow(() -> new RuntimeException("Beauty salon not found"));
        List<Review> beautySalonReviews = reviewRepository.getReviewsByBeautySalon(beautySalon);
        return beautySalonReviews.stream()
                .map(review -> new ReviewDto()
                        .setId(review.getId())
                        .setComment(review.getComment()))
                .toList();
    }

    public Review addReview(ReviewDto reviewDto) {

    }

    public Review updateReview(UUID salonId, UUID reviewId, ReviewDto reviewDto) {
    }

    public void deleteReview(UUID salonId, UUID reviewId) {
    }
}
