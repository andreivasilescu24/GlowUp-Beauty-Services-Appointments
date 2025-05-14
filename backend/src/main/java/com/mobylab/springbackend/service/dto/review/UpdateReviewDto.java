package com.mobylab.springbackend.service.dto.review;

public class UpdateReviewDto {
    private String comment;
    private int rating;

    public String getComment() {
        return comment;
    }

    public UpdateReviewDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public UpdateReviewDto setRating(int rating) {
        this.rating = rating;
        return this;
    }
}
