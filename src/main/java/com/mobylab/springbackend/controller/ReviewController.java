package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.service.ReviewService;
import com.mobylab.springbackend.service.dto.review.CreateReviewDto;
import com.mobylab.springbackend.service.dto.review.ReviewDto;
import com.mobylab.springbackend.service.dto.review.UpdateReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon/{salonId}/reviews")
public class ReviewController implements SecuredRestController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable UUID salonId) {
        List<ReviewDto> reviewsDtoList = reviewService.getAllReviews(salonId);
        return ResponseEntity.status(200).body(reviewsDtoList);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReviewDto> addReview(@PathVariable UUID salonId, @RequestBody CreateReviewDto createReviewDto) {
        ReviewDto createdReview = reviewService.addReview(salonId, createReviewDto);
        return ResponseEntity.status(201).body(createdReview);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable UUID salonId,
                                               @PathVariable UUID reviewId,
                                               @RequestBody UpdateReviewDto updateReviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(salonId, reviewId, updateReviewDto);
        return ResponseEntity.status(200).body(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> deleteReview(@PathVariable UUID salonId,
                                               @PathVariable UUID reviewId) {
        reviewService.deleteReview(salonId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
