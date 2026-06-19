package com.bridgelabz.service.impl;
import com.bridgelabz.dto.*;
import com.bridgelabz.entity.*;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.*;
import com.bridgelabz.security.SecurityUtils;
import com.bridgelabz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse addReview(
            ReviewRequest request) {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Product product =
                productRepository.findById(
                                request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        boolean alreadyReviewed =
                reviewRepository
                        .findByUserIdAndProductId(
                                user.getId(),
                                product.getId())
                        .isPresent();

        if (alreadyReviewed) {
            throw new RuntimeException(
                    "You already reviewed this product");
        }

        if (request.getRating() < 1 ||
                request.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview =
                reviewRepository.save(review);

        return ReviewResponse.builder()
                .userName(
                        savedReview.getUser().getName())
                .rating(
                        savedReview.getRating())
                .comment(
                        savedReview.getComment())
                .build();
    }

    @Override
    public ProductReviewResponse getReviews(
            Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        List<Review> reviews =
                reviewRepository.findByProductId(
                        productId);

        double averageRating =
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

        List<ReviewResponse> reviewResponses =
                reviews.stream()
                        .map(review ->
                                ReviewResponse.builder()
                                        .userName(
                                                review.getUser()
                                                        .getName())
                                        .rating(
                                                review.getRating())
                                        .comment(
                                                review.getComment())
                                        .build())
                        .toList();

        return ProductReviewResponse.builder()
                .productId(product.getId())
                .productTitle(product.getTitle())
                .averageRating(averageRating)
                .reviews(reviewResponses)
                .build();
    }
}
