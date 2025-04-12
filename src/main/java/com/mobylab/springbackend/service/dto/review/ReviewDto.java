package com.mobylab.springbackend.service.dto.review;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDto {
    private UUID id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private String clientName;

    public UUID getId() {
        return id;
    }

    public ReviewDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ReviewDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public ReviewDto setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public ReviewDto setClientId(String clientName) {
        this.clientName = clientName;
        return this;
    }
}
    