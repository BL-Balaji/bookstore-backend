package com.bridgelabz.service;
import com.bridgelabz.dto.*;

public interface ReviewService {

    ReviewResponse addReview(
            ReviewRequest request);

    ProductReviewResponse getReviews(
            Long productId);
}
