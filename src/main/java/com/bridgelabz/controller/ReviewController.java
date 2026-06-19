package com.bridgelabz.controller;
import com.bridgelabz.dto.*;
import com.bridgelabz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponse addReview(
            @RequestBody ReviewRequest request) {

        return reviewService.addReview(request);
    }

    @GetMapping("/{productId}")
    public ProductReviewResponse getReviews(
            @PathVariable Long productId) {

        return reviewService.getReviews(productId);
    }
}
