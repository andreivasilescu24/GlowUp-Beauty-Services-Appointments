package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.service.ReviewService;
import com.mobylab.springbackend.service.dto.review.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beautySalon/{salonId}/reviews")
public class ReviewController {
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
    public ResponseEntity<Review> addReview(@RequestBody ReviewDto reviewDto) {
        Review createdReview = reviewService.addReview(reviewDto);
        return ResponseEntity.status(201).body(createdReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable UUID salonId, @PathVariable UUID reviewId, @RequestBody ReviewDto reviewDto) {
        Review updatedReview = reviewService.updateReview(salonId, reviewId, reviewDto);
        return ResponseEntity.status(200).body(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable UUID salonId, @PathVariable UUID reviewId) {
        reviewService.deleteReview(salonId, reviewId);
        return ResponseEntity.noContent().build();
    }

}
