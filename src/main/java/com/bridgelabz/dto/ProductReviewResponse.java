package com.bridgelabz.dto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewResponse {

    private Long productId;

    private String productTitle;

    private Double averageRating;

    private List<ReviewResponse> reviews;
}
