package com.mobylab.springbackend.service.dto.review;

import java.util.UUID;

public class CreateReviewDto {
    private UUID clientId;
    private String comment;
    private int rating;

    public UUID getClientId() {
        return clientId;
    }

    public CreateReviewDto setClientId(UUID clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CreateReviewDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public CreateReviewDto setRating(int rating) {
        this.rating = rating;
        return this;
    }
}
