package com.mobylab.springbackend.service.dto.review;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDto {
    private UUID id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private UUID clientId;
}
    